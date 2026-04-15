package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvMoveEffectRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.MoveEffectRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MoveEffectService implements IPokemonDataLoader {

    private final CsvMoveEffectRepositoryImpl csvMoveEffectRepository;
    private final MoveEffectRepository moveEffectRepository;


    public MoveEffectService(CsvMoveEffectRepositoryImpl csvMainRepo, 
                           MoveEffectRepository moveEffectRepository) {
        this.csvMoveEffectRepository = csvMainRepo;
        this.moveEffectRepository = moveEffectRepository;
    }

    @Override
    public String getEntityName() { return "MoveEffect"; }

    @Override
    @Transactional
    public void loadNodes() {
        List<Map<String, Object>> rows = csvMoveEffectRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("shortEffect", csv.getShortEffect());
                    row.put("effect", csv.getEffect());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            moveEffectRepository.batchInsertMoveEffects(rows);
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
