package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvMoveMetaAilmentsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.MoveAilmentRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MoveAilmentService implements IPokemonDataLoader {

    private final CsvMoveMetaAilmentsRepositoryImpl csvMoveMetaAilmentsRepository;
    private final MoveAilmentRepository moveAilmentRepository;

    public MoveAilmentService(CsvMoveMetaAilmentsRepositoryImpl csvMoveMetaAilmentsRepository,
                           MoveAilmentRepository moveAilmentRepository) {
        this.csvMoveMetaAilmentsRepository = csvMoveMetaAilmentsRepository;
        this.moveAilmentRepository = moveAilmentRepository;
    }

    @Override
    public String getEntityName() { return "MoveAilment"; }

    @Override
    @Transactional
    public void loadNodes() {
        List<Map<String, Object>> rows = csvMoveMetaAilmentsRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("identifier", csv.getIdentifier());
                    row.put("name", csv.getName());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            Integer count = moveAilmentRepository.batchInsertMoveAilments(rows);
            System.out.println("Successfully loaded " + count + " MoveAilments nodes.");
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
