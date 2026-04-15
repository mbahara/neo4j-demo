package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvEncounterMethodsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.EncounterMethodRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EncounterMethodService implements IPokemonDataLoader {

    private final CsvEncounterMethodsRepositoryImpl csvMainRepo;
    private final EncounterMethodRepository neo4jRepo;

    public EncounterMethodService(CsvEncounterMethodsRepositoryImpl csvMainRepo,
                           EncounterMethodRepository neo4jRepo) {
        this.csvMainRepo = csvMainRepo;
        this.neo4jRepo = neo4jRepo;
    }

    @Override
    public String getEntityName() { return "EncounterMethod"; }

    @Override
    @Transactional
    public void loadNodes() {
        List<Map<String, Object>> rows = csvMainRepo.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("identifier", csv.getIdentifier());
                    row.put("name", csv.getName());
                    row.put("order", csv.getOrder());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            neo4jRepo.batchInsertEncounterMethods(rows);
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
