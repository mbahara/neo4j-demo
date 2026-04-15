package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvMoveBattleStylesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.MoveBattleStyleRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MoveBattleStyleService implements IPokemonDataLoader {

    private final CsvMoveBattleStylesRepositoryImpl csvMoveBattleStylesRepository;
    private final MoveBattleStyleRepository moveBattleStyleRepository;

    public MoveBattleStyleService(CsvMoveBattleStylesRepositoryImpl csvMoveBattleStylesRepository,
                           MoveBattleStyleRepository moveBattleStyleRepository) {
        this.csvMoveBattleStylesRepository = csvMoveBattleStylesRepository;
        this.moveBattleStyleRepository = moveBattleStyleRepository;
    }

    @Override
    public String getEntityName() { return "MoveBattleStyle"; }

    @Override
    @Transactional
    public void loadNodes() {
        List<Map<String, Object>> rows = csvMoveBattleStylesRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("identifier", csv.getIdentifier());
                    row.put("name", csv.getName());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            moveBattleStyleRepository.batchInsertMoveBattleStyles(rows);
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
