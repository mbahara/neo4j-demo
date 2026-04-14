import os
import re

# Configuration
REPO_NEO4J_DIR = r"C:\Users\bmuradi\Nextcloud\Documents\neo4j-demo\src\main\java\at\jku\faw\neo4jdemo\repository\neo4j"
REPO_CSV_DIR = r"C:\Users\bmuradi\Nextcloud\Documents\neo4j-demo\src\main\java\at\jku\faw\neo4jdemo\repository\csv"
SERVICE_OUTPUT_DIR = r"C:\Users\bmuradi\Nextcloud\Documents\neo4j-demo\src\main\java\at\jku\faw\neo4jdemo\service\pokemon"

SERVICE_TEMPLATE = """package at.jku.faw.neo4jdemo.service.pokemon;

{imports}
import at.jku.faw.neo4jdemo.service.pokemon.IPokemonDataLoader;

@Service
public class {class_name}Service implements IDataLoader {{

    private final Csv{class_name}RepositoryImpl csvMainRepo;
    private final {class_name}Repository neo4jRepo;
{injected_repos}

    public {class_name}Service(Csv{class_name}RepositoryImpl csvMainRepo, 
                           {class_name}Repository neo4jRepo,
                           {constructor_params}) {{
        this.csvMainRepo = csvMainRepo;
        this.neo4jRepo = neo4jRepo;
{constructor_assignments}
    }}

    @Override
    public String getEntityName() {{ return "{class_name}"; }}

    @Override
    @Transactional
    public void loadNodes() {{
        csvMainRepo.getAll().forEach(csv -> {{
            neo4jRepo.insert{class_name}(csv.id(), {insert_args});
        }});
    }}

    @Override
    @Transactional
    public void loadRelationships() {{
{relationship_logic}
    }}
}}
"""

def generate_services():
    if not os.listdir(REPO_NEO4J_DIR):
        print("No repositories found.")
        return

    for repo_file in os.listdir(REPO_NEO4J_DIR):
        if not repo_file.endswith("Repository.java"): continue
        
        class_name = repo_file.replace("Repository.java", "")
        with open(os.path.join(REPO_NEO4J_DIR, repo_file), 'r', encoding='utf-8') as f:
            repo_content = f.read()

        # 1. Parse Insert Arguments 
        insert_method_pattern = rf'{class_name}\s+insert{class_name}\(@Param\("id"\) Long id,\s*(.*?)\);'
        insert_match = re.search(insert_method_pattern, repo_content, re.DOTALL)
        
        insert_args = ""
        if insert_match:
            # Extract names from @Param("name") Type name
            params = re.findall(r'@Param\("\w+"\)\s+[\w<>|]+\s+(\w+)', insert_match.group(1))
            insert_args = ", ".join([f"csv.{p}()" for p in params])

        # 2. Parse Link Methods
        link_methods = re.findall(r'void (link\w+To(\w+))\((.*?)\);', repo_content, re.DOTALL)
        
        injections, assignments, c_params, rel_logic = [], [], [], []
        imports = {
            "import org.springframework.stereotype.Service;",
            "import org.springframework.transaction.annotation.Transactional;",
            f"import at.jku.faw.neo4jdemo.repository.neo4j.{class_name}Repository;",
            f"import at.jku.faw.neo4jdemo.repository.csv.Csv{class_name}RepositoryImpl;",
            f"import at.jku.faw.neo4jdemo.model.csv.Csv{class_name};"
        }

        for method_name, target_node, params_text in link_methods:
            link_params = re.findall(r'@Param\("\w+"\)\s+\w+\s+(\w+)', params_text)
            
            # CASE A: Separate Mapping Table CSV
            csv_repo_class = f"Csv{class_name}{target_node}RepositoryImpl"
            if os.path.exists(os.path.join(REPO_CSV_DIR, f"{csv_repo_class}.java")):
                var_name = csv_repo_class[0].lower() + csv_repo_class[1:]
                injections.append(f"    private final {csv_repo_class} {var_name};")
                c_params.append(f"{csv_repo_class} {var_name}")
                assignments.append(f"        this.{var_name} = {var_name};")
                imports.add(f"import at.jku.faw.neo4jdemo.repository.csv.{csv_repo_class};")

                # Mapping Table Logic
                rel_logic.append(f"        csvMainRepo.getAll().forEach(main -> {{")
                rel_logic.append(f"            {var_name}.getAll().stream()")
                rel_logic.append(f"                .filter(m -> java.util.Objects.equals(m.{class_name[0].lower()}{class_name[1:]}Id(), main.id()))")
                rel_logic.append(f"                .forEach(m -> neo4jRepo.{method_name}({', '.join(['main.id()'] + [f'm.{p}()' for p in link_params if p.lower() != class_name.lower() + 'id'])}));")
                rel_logic.append(f"        }});")
            
            # CASE B: Direct Relationship in Main CSV
            else:
                target_param = f"{target_node[0].lower()}{target_node[1:]}Id"
                rel_logic.append(f"        csvMainRepo.getAll().forEach(csv -> {{")
                rel_logic.append(f"            if (csv.{target_param}() != null) {{")
                rel_logic.append(f"                neo4jRepo.{method_name}(csv.id(), csv.{target_param}());")
                rel_logic.append(f"            }}")
                rel_logic.append(f"        }});")

        final_rel_logic = "\n".join(rel_logic) if rel_logic else "        // No relationships to load."

        # Manage constructor commas
        constructor_params_str = ", ".join(c_params)
        if constructor_params_str:
            constructor_params_str = ", " + constructor_params_str

        content = SERVICE_TEMPLATE.format(
            imports="\n".join(sorted(list(imports))),
            class_name=class_name,
            injected_repos="\n".join(injections),
            constructor_params=constructor_params_str,
            constructor_assignments="\n".join(assignments),
            insert_args=insert_args,
            relationship_logic=final_rel_logic
        )

        with open(os.path.join(SERVICE_OUTPUT_DIR, f"{class_name}Service.java"), "w", encoding="utf-8") as f:
            f.write(content)
    
    print("Services generated successfully with 'insert' pattern.")

if __name__ == "__main__":
    generate_services()