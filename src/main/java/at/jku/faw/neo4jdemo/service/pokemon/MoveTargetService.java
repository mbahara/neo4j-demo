package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvMoveTargetsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.MoveTargetRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MoveTargetService implements IPokemonDataLoader {

    private final CsvMoveTargetsRepositoryImpl csvMoveTargetsRepository;
    private final MoveTargetRepository moveTargetRepository;


    public MoveTargetService(CsvMoveTargetsRepositoryImpl csvMoveTargetsRepository,
                             MoveTargetRepository moveTargetRepository) {
        this.csvMoveTargetsRepository = csvMoveTargetsRepository;
        this.moveTargetRepository = moveTargetRepository;

    }

    @Override
    public String getEntityName() { return "MoveTarget"; }

    @Override
    @Transactional
    public void loadNodes() {
        List<Map<String, Object>> rows = csvMoveTargetsRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("identifier", csv.getIdentifier());
                    row.put("name", csv.getName());
                    row.put("description", csv.getDescription());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            moveTargetRepository.batchInsertMoveTargets(rows);
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
