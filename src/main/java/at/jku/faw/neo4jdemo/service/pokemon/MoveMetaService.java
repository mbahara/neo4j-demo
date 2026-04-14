package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvMoveMetaRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.MoveCategoryRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.MoveMetaRepository;
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
        csvMoveMetaRepository.getAll().forEach(csv -> {
            moveMetaRepository.insertMoveMeta(csv.getMinHits(), csv.getMaxHits(), csv.getMinTurns(), csv.getMaxTurns(), csv.getDrain(), csv.getHealing(), csv.getCritRate(), csv.getAilmentChance(), csv.getFlinchChance(), csv.getStatChance());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvMoveMetaRepository.getAll().forEach(csvMoveMeta -> {
            if (csvMoveMeta.getMetaAilmentId() != null) {
                moveMetaRepository.findById(csvMoveMeta.getMetaAilmentId())
                        .ifPresent(moveMeta -> {
                            moveMetaRepository.linkMoveMetaToMoveAilment(moveMeta.getId(), csvMoveMeta.getMetaAilmentId());
                        });
            }
            if (csvMoveMeta.getMetaCategoryId() != null) {
                moveCategoryRepository.findById(csvMoveMeta.getMetaCategoryId())
                        .ifPresent(moveCategory -> moveMetaRepository.linkMoveMetaToMoveCategory(csvMoveMeta.getMetaCategoryId(), moveCategory.getId()));
            }
        });
    }
}
