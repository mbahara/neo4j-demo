package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.neo4j.MoveMeta;
import at.jku.faw.neo4jdemo.repository.csv.CsvMoveMetaRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.MoveCategoryRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.MoveMetaRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MoveMetaService implements IPokemonDataLoader {

    private final CsvMoveMetaRepositoryImpl csvMoveMetaRepository;
    private final MoveMetaRepository moveMetaRepository;
    private final MoveCategoryRepository moveCategoryRepository;

    public MoveMetaService(CsvMoveMetaRepositoryImpl csvMoveMetaRepository,
                           MoveMetaRepository moveMetaRepository, MoveCategoryRepository moveCategoryRepository) {
        this.csvMoveMetaRepository = csvMoveMetaRepository;
        this.moveMetaRepository = moveMetaRepository;
        this.moveCategoryRepository = moveCategoryRepository;
    }

    @Override
    public String getEntityName() { return "MoveMeta"; }

    @Override
    @Transactional
    public void loadNodes() {
        List<Map<String, Object>> rows = csvMoveMetaRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("moveId", csv.getMoveId());
                    row.put("maxHits", csv.getMaxHits());
                    row.put("minTurns", csv.getMinTurns());
                    row.put("maxTurns", csv.getMaxTurns());
                    row.put("drain", csv.getDrain());
                    row.put("healing", csv.getHealing());
                    row.put("critRate", csv.getCritRate());
                    row.put("ailmentChance", csv.getAilmentChance());
                    row.put("flinchChance", csv.getFlinchChance());
                    row.put("statChance", csv.getStatChance());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            moveMetaRepository.batchInsertMoveMeta(rows);
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvMoveMetaRepository.getAll().forEach(csvMoveMeta -> {
            if (csvMoveMeta.getMetaAilmentId() != null) {
                moveMetaRepository.findById(csvMoveMeta.getMetaAilmentId())
                        .ifPresent(moveMeta ->
                            moveMetaRepository.linkMoveMetaToMoveAilment(moveMeta.getId(), csvMoveMeta.getMetaAilmentId())
                        );
            }
            if (csvMoveMeta.getMetaCategoryId() != null) {
                moveCategoryRepository.findById(csvMoveMeta.getMetaCategoryId())
                        .ifPresent(moveCategory -> moveMetaRepository.linkMoveMetaToMoveCategory(csvMoveMeta.getMetaCategoryId(), moveCategory.getId()));
            }
        });

        Map<Long, MoveMeta> metaLookup = moveMetaRepository.findAll().stream()
                .collect(Collectors.toMap(
                        MoveMeta::getMoveId,
                        m -> m
                ));

        csvMoveMetaRepository.getAll().forEach(csv -> {
            MoveMeta node = metaLookup.get(csv.getMoveId());

            if (node != null) {
                if (csv.getMetaAilmentId() != null) {
                    moveMetaRepository.linkMoveMetaToMoveAilment(node.getId(), csv.getMetaAilmentId());
                }
                if (csv.getMetaCategoryId() != null) {
                    moveMetaRepository.linkMoveMetaToMoveCategory(node.getId(), csv.getMetaCategoryId());
                }
            }
        });
    }
}
