package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.neo4j.MoveChange;
import at.jku.faw.neo4jdemo.repository.csv.CsvMoveChangelogRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.MoveChangeRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MoveChangeService implements IPokemonDataLoader {

    private final CsvMoveChangelogRepositoryImpl csvMoveChangelogRepositoryImpl;
    private final MoveChangeRepository moveChangeRepository;

    public MoveChangeService(CsvMoveChangelogRepositoryImpl csvMoveChangelogRepositoryImpl,
                             MoveChangeRepository moveChangeRepository) {
        this.csvMoveChangelogRepositoryImpl = csvMoveChangelogRepositoryImpl;
        this.moveChangeRepository = moveChangeRepository;
    }

    @Override
    public String getEntityName() { return "MoveChange"; }

    @Override
    @Transactional
    public void loadNodes() {
        List<Map<String, Object>> rows = csvMoveChangelogRepositoryImpl.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("power", csv.getPower());
                    row.put("pp", csv.getPp());
                    row.put("accuracy", csv.getAccuracy());
                    row.put("priority", csv.getPriority());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            moveChangeRepository.batchInsertMoveChanges(rows);
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        Map<Long, List<MoveChange>> moveChangeMap = moveChangeRepository.findAll().stream()
                .collect(Collectors.groupingBy(MoveChange::getId));

        csvMoveChangelogRepositoryImpl.getAll().forEach(csv -> {
            List<MoveChange> matchingNodes = moveChangeMap.get(csv.getMoveId());

            if (matchingNodes != null) {
                matchingNodes.forEach(moveChange -> {
                    Long internalId = moveChange.getId();

                    if (csv.getVersionGroupId() != null) {
                        moveChangeRepository.linkMoveChangeToVersionGroup(internalId, csv.getVersionGroupId());
                    }
                    if (csv.getTypeId() != null) {
                        moveChangeRepository.linkMoveChangeToType(internalId, csv.getTypeId());
                    }
                    if (csv.getEffectId() != null) {
                        moveChangeRepository.linkMoveChangeToMoveEffect(internalId, csv.getEffectId(), csv.getEffectChance());
                    }
                    if (csv.getTargetId() != null) {
                        moveChangeRepository.linkMoveChangeToMoveTarget(internalId, csv.getTargetId());
                    }
                });
            }
        });
    }
}
