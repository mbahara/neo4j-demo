package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvMoveFlagsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.MoveFlagRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MoveFlagService implements IPokemonDataLoader {

    private final CsvMoveFlagsRepositoryImpl csvMoveFlagsRepository;
    private final MoveFlagRepository moveFlagRepository;


    public MoveFlagService(CsvMoveFlagsRepositoryImpl csvMoveFlagsRepository,
                           MoveFlagRepository neo4jRepo) {
        this.csvMoveFlagsRepository = csvMoveFlagsRepository;
        this.moveFlagRepository = neo4jRepo;
    }

    @Override
    public String getEntityName() { return "MoveFlag"; }

    @Override
    @Transactional
    public void loadNodes() {
        List<Map<String, Object>> rows = csvMoveFlagsRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("identifier", csv.getIdentifier());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            moveFlagRepository.batchInsertMoveFlags(rows);
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
