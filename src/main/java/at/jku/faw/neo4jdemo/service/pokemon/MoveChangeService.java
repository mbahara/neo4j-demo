package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvMoveChangelogRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.MoveChangeRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.MoveEffectRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.MoveTargetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MoveChangeService implements IPokemonDataLoader {

    private final CsvMoveChangelogRepositoryImpl csvMoveChangelogRepository;
    private final MoveChangeRepository moveChangeRepository;
    private final MoveEffectRepository moveEffectRepository;
    private final MoveTargetRepository moveTargetRepository;

    public MoveChangeService(CsvMoveChangelogRepositoryImpl csvMoveChangelogRepository,
                             MoveChangeRepository moveChangeRepository, MoveEffectRepository moveEffectRepository,
                             MoveTargetRepository moveTargetRepository) {
        this.csvMoveChangelogRepository = csvMoveChangelogRepository;
        this.moveChangeRepository = moveChangeRepository;
        this.moveEffectRepository = moveEffectRepository;
        this.moveTargetRepository = moveTargetRepository;
    }

    @Override
    public String getEntityName() { return "MoveChange"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvMoveChangelogRepository.getAll().forEach(csv -> {
            moveChangeRepository.insertMoveChange(csv.getPower(), csv.getPp(), csv.getAccuracy(), csv.getPriority());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvMoveChangelogRepository.getAll().forEach(csvMoveChangelog -> {
            moveChangeRepository.findAll().forEach(moveChange -> {
                if (csvMoveChangelog.getVersionGroupId() != null) {
                    moveChangeRepository.linkMoveChangeToVersionGroup(moveChange.getId(), csvMoveChangelog.getVersionGroupId());
                }
                if (csvMoveChangelog.getTypeId() != null) {
                    moveChangeRepository.linkMoveChangeToType(moveChange.getId(), csvMoveChangelog.getTypeId());
                }
                if (csvMoveChangelog.getEffectId() != null) {
                    moveEffectRepository.findById(csvMoveChangelog.getEffectId()).ifPresent(moveEffect ->
                        moveChangeRepository.linkMoveChangeToMoveEffect(moveChange.getId(), moveEffect.getId(), csvMoveChangelog.getEffectChance())
                    );
                }
                if (csvMoveChangelog.getTargetId() != null) {
                    moveTargetRepository.findById(csvMoveChangelog.getTargetId()).ifPresent(moveTarget ->
                        moveChangeRepository.linkMoveChangeToMoveTarget(moveChange.getId(), moveTarget.getId())
                    );
                }
            });
        });
    }
}
