package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvContestCombosRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvMoveChangelogRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvMoveFlagMapRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvMoveRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvSuperContestCombosRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.EffectRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.MoveChangeRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.MoveMetaRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.MoveRepository;
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
        csvMoveRepository.getAll().forEach(csv -> {
            moveRepository.insertMove(csv.getId(), csv.getName(), csv.getPower(), csv.getPp(), csv.getAccuracy(), csv.getPriority());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvMoveRepository.getAll().forEach(csvMove -> {
            if (csvMove.getTypeId() != null) {
                moveRepository.linkMoveToType(csvMove.getId(), csvMove.getTypeId());
            }
            if (csvMove.getGenerationId() != null) {
                moveRepository.linkMoveToGeneration(csvMove.getId(), csvMove.getGenerationId());
            }
            if (csvMove.getDamageClassId() != null) {
                moveRepository.linkMoveToDamageClass(csvMove.getId(), csvMove.getDamageClassId());
            }
            if (csvMove.getEffectId() != null) {
                effectRepository.linkMoveToMoveEffect(csvMove.getId(), csvMove.getEffectId(), csvMove.getEffectChance());
            }
            if (csvMove.getTargetId() != null) {
                moveRepository.linkMoveToMoveTarget(csvMove.getId(), csvMove.getTargetId());
            }
            csvMoveFlagMapRepositoryImpl.getByMoveId(csvMove.getId()).forEach(csvMoveFlagMap -> {
                moveRepository.linkMoveToMoveFlag(csvMoveFlagMap.getMoveId(), csvMoveFlagMap.getMoveFlagId());
            });
            moveMetaRepository.findAll().stream()
                    .filter(moveMeta -> moveMeta.getId().equals(csvMove.getId()))
                    .forEach(moveMeta -> moveRepository.linkMoveToMoveMeta(csvMove.getId(), moveMeta.getId()));

            moveChangeRepository.findAll().forEach(moveChange ->
                csvMoveChangelogRepositoryImpl.getByMoveId(csvMove.getId()).forEach(csvMoveChangelog -> {
                    moveRepository.linkMoveToMoveChange(csvMoveChangelog.getMoveId(), moveChange.getId());
                }));

            if (csvMove.getContestTypeId() != null) {
                moveRepository.linkMoveToContestType(csvMove.getId(), csvMove.getContestTypeId());
            }

            if (csvMove.getContestEffectId() != null) {
                moveRepository.linkMoveToContestEffect(csvMove.getId(), csvMove.getContestEffectId());
            }

            csvContestCombosRepositoryImpl.getAll().forEach(contestCombo -> {
               moveRepository.linkMoveToComboMove(contestCombo.getFirstMoveId(), contestCombo.getSecondMoveId());
            });

            csvSuperContestCombosRepositoryImpl.getAll().forEach(superContestCombo -> {
                moveRepository.linkMoveToComboMoveInSuperContest(superContestCombo.getFirstMoveId(),
                        superContestCombo.getSecondMoveId());
            });
        });
    }
}
