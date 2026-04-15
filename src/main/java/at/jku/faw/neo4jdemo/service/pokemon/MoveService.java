package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvMoveChangelog;
import at.jku.faw.neo4jdemo.model.csv.CsvMoveFlagMap;
import at.jku.faw.neo4jdemo.model.neo4j.MoveChange;
import at.jku.faw.neo4jdemo.model.neo4j.MoveMeta;
import at.jku.faw.neo4jdemo.repository.csv.CsvContestCombosRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvMoveChangelogRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvMoveFlagMapRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvMoveRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvSuperContestCombosRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.EffectRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.MoveChangeRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.MoveMetaRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.MoveRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MoveService implements IPokemonDataLoader {

    private final CsvMoveRepositoryImpl csvMoveRepository;
    private final MoveRepository moveRepository;
    private final CsvMoveFlagMapRepositoryImpl csvMoveFlagMapRepositoryImpl;
    private final MoveMetaRepository moveMetaRepository;
    private final MoveChangeRepository moveChangeRepository;
    private final CsvMoveChangelogRepositoryImpl csvMoveChangelogRepositoryImpl;
    private final CsvSuperContestCombosRepositoryImpl csvSuperContestCombosRepositoryImpl;
    private final CsvContestCombosRepositoryImpl csvContestCombosRepositoryImpl;
    private final EffectRepository effectRepository;

    public MoveService(CsvMoveRepositoryImpl csvMoveRepository,
                       MoveRepository moveRepository, CsvMoveFlagMapRepositoryImpl csvMoveFlagMapRepositoryImpl,
                       MoveMetaRepository moveMetaRepository,
                       MoveChangeRepository moveChangeRepository,
                       CsvMoveChangelogRepositoryImpl csvMoveChangelogRepositoryImpl,
                       CsvSuperContestCombosRepositoryImpl csvSuperContestCombosRepositoryImpl,
                       CsvContestCombosRepositoryImpl csvContestCombosRepositoryImpl, EffectRepository effectRepository) {
        this.csvMoveRepository = csvMoveRepository;
        this.moveRepository = moveRepository;
        this.csvMoveFlagMapRepositoryImpl = csvMoveFlagMapRepositoryImpl;
        this.moveMetaRepository = moveMetaRepository;
        this.moveChangeRepository = moveChangeRepository;
        this.csvMoveChangelogRepositoryImpl = csvMoveChangelogRepositoryImpl;
        this.csvSuperContestCombosRepositoryImpl = csvSuperContestCombosRepositoryImpl;
        this.csvContestCombosRepositoryImpl = csvContestCombosRepositoryImpl;
        this.effectRepository = effectRepository;
    }

    @Override
    public String getEntityName() { return "Move"; }

    @Override
    @Transactional
    public void loadNodes() {
        List<Map<String, Object>> rows = csvMoveRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("name", csv.getName());
                    row.put("power", csv.getPower());
                    row.put("pp", csv.getPp());
                    row.put("accuracy", csv.getAccuracy());
                    row.put("priority", csv.getPriority());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            Integer count = moveRepository.batchInsertMoves(rows);
            System.out.println("Successfully loaded " + count + " Moves nodes.");
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        Map<Long, List<CsvMoveFlagMap>> flagMap = csvMoveFlagMapRepositoryImpl.getAll().stream()
                .collect(Collectors.groupingBy(CsvMoveFlagMap::getMoveId));

        Map<Long, List<CsvMoveChangelog>> changelogMap = csvMoveChangelogRepositoryImpl.getAll().stream()
                .collect(Collectors.groupingBy(CsvMoveChangelog::getMoveId));

        Map<Long, Long> moveMetaLookup = moveMetaRepository.findAll().stream()
                .collect(Collectors.toMap(MoveMeta::getId, MoveMeta::getId));

        List<MoveChange> allMoveChanges = moveChangeRepository.findAll();

        csvMoveRepository.getAll().forEach(csv -> {
            Long mId = csv.getId();

            if (csv.getTypeId() != null) moveRepository.linkMoveToType(mId, csv.getTypeId());
            if (csv.getGenerationId() != null) moveRepository.linkMoveToGeneration(mId, csv.getGenerationId());
            if (csv.getDamageClassId() != null) moveRepository.linkMoveToDamageClass(mId, csv.getDamageClassId());
            if (csv.getEffectId() != null) effectRepository.linkMoveToMoveEffect(mId, csv.getEffectId(), csv.getEffectChance());
            if (csv.getTargetId() != null) moveRepository.linkMoveToMoveTarget(mId, csv.getTargetId());
            if (csv.getContestTypeId() != null) moveRepository.linkMoveToContestType(mId, csv.getContestTypeId());
            if (csv.getContestEffectId() != null) moveRepository.linkMoveToContestEffect(mId, csv.getContestEffectId());

            if (flagMap.containsKey(mId)) {
                flagMap.get(mId).forEach(f -> moveRepository.linkMoveToMoveFlag(mId, f.getMoveFlagId()));
            }

            if (moveMetaLookup.containsKey(mId)) {
                moveRepository.linkMoveToMoveMeta(mId, moveMetaLookup.get(mId));
            }

            if (changelogMap.containsKey(mId)) {
                changelogMap.get(mId).forEach(log ->
                        allMoveChanges.forEach(change -> moveRepository.linkMoveToMoveChange(mId, change.getId()))
                );
            }
        });

        csvContestCombosRepositoryImpl.getAll().forEach(combo ->
                moveRepository.linkMoveToComboMove(combo.getFirstMoveId(), combo.getSecondMoveId())
        );

        csvSuperContestCombosRepositoryImpl.getAll().forEach(superCombo ->
                moveRepository.linkMoveToComboMoveInSuperContest(superCombo.getFirstMoveId(), superCombo.getSecondMoveId())
        );
    }
}
