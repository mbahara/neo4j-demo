package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvPokeathlonStatsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.PokeathlonStatsRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PokeathlonStatsService implements IPokemonDataLoader {

    private final CsvPokeathlonStatsRepositoryImpl csvMainRepo;
    private final PokeathlonStatsRepository neo4jRepo;


    public PokeathlonStatsService(CsvPokeathlonStatsRepositoryImpl csvMainRepo, 
                           PokeathlonStatsRepository neo4jRepo) {
        this.csvMainRepo = csvMainRepo;
        this.neo4jRepo = neo4jRepo;

    }

    @Override
    public String getEntityName() { return "PokeathlonStats"; }

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
            neo4jRepo.batchInsertPokeathlonStats(rows);
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
