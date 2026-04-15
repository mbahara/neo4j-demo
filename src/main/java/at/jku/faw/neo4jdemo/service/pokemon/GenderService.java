package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvGendersRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.GenderRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GenderService implements IPokemonDataLoader {

    private final CsvGendersRepositoryImpl csvMainRepo;
    private final GenderRepository neo4jRepo;

    public GenderService(CsvGendersRepositoryImpl csvMainRepo,
                           GenderRepository neo4jRepo) {
        this.csvMainRepo = csvMainRepo;
        this.neo4jRepo = neo4jRepo;
    }

    @Override
    public String getEntityName() { return "Gender"; }

    @Override
    @Transactional
    public void loadNodes() {
        List<Map<String, Object>> rows = csvMainRepo.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("identifier", csv.getIdentifier());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            Integer count = neo4jRepo.batchInsertGenders(rows);
            System.out.println("Successfully loaded " + count + " Genders nodes.");
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
