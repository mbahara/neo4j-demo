package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvEvolutionTriggersRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.EvolutionTriggerRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EvolutionTriggerService implements IPokemonDataLoader {

    private final CsvEvolutionTriggersRepositoryImpl csvMainRepo;
    private final EvolutionTriggerRepository neo4jRepo;


    public EvolutionTriggerService(CsvEvolutionTriggersRepositoryImpl csvMainRepo,
                           EvolutionTriggerRepository neo4jRepo) {
        this.csvMainRepo = csvMainRepo;
        this.neo4jRepo = neo4jRepo;
    }

    @Override
    public String getEntityName() { return "EvolutionTrigger"; }

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
            neo4jRepo.batchInsertEvolutionTriggers(rows);
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
