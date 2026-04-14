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
            moveRepository.insertMove(csv.id(), csv.name(), csv.power(), csv.pp(), csv.accuracy(), csv.priority());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvMoveRepository.getAll().forEach(csvMove -> {
            if (csvMove.typeId() != null) {
                moveRepository.linkMoveToType(csvMove.id(), csvMove.typeId());
            }
            if (csvMove.generationId() != null) {
                moveRepository.linkMoveToGeneration(csvMove.id(), csvMove.generationId());
            }
            if (csvMove.damageClassId() != null) {
                moveRepository.linkMoveToDamageClass(csvMove.id(), csvMove.damageClassId());
            }
            if (csvMove.effectId() != null) {
                effectRepository.linkMoveToMoveEffect(csvMove.id(), csvMove.effectId(), csvMove.effectChance());
            }
            if (csvMove.targetId() != null) {
                moveRepository.linkMoveToMoveTarget(csvMove.id(), csvMove.targetId());
            }
            csvMoveFlagMapRepositoryImpl.getByMoveId(csvMove.id()).forEach(csvMoveFlagMap -> {
                moveRepository.linkMoveToMoveFlag(csvMoveFlagMap.moveId(), csvMoveFlagMap.moveFlagId());
            });
            moveMetaRepository.findAll().stream()
                    .filter(moveMeta -> moveMeta.getId().equals(csvMove.id()))
                    .forEach(moveMeta -> moveRepository.linkMoveToMoveMeta(csvMove.id(), moveMeta.getId()));

            moveChangeRepository.findAll().forEach(moveChange ->
                csvMoveChangelogRepositoryImpl.getByMoveId(csvMove.id()).forEach(csvMoveChangelog -> {
                    moveRepository.linkMoveToMoveChange(csvMoveChangelog.moveId(), moveChange.getId());
                }));

            if (csvMove.contestTypeId() != null) {
                moveRepository.linkMoveToContestType(csvMove.id(), csvMove.contestTypeId());
            }

            if (csvMove.contestEffectId() != null) {
                moveRepository.linkMoveToContestEffect(csvMove.id(), csvMove.contestEffectId());
            }

            csvContestCombosRepositoryImpl.getAll().forEach(contestCombo -> {
               moveRepository.linkMoveToComboMove(contestCombo.firstMoveId(), contestCombo.secondMoveId());
            });

            csvSuperContestCombosRepositoryImpl.getAll().forEach(superContestCombo -> {
                moveRepository.linkMoveToComboMoveInSuperContest(superContestCombo.firstMoveId(),
                        superContestCombo.secondMoveId());
            });
        });
    }
}
