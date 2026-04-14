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
            moveMetaRepository.insertMoveMeta(csv.minHits(), csv.maxHits(), csv.minTurns(), csv.maxTurns(), csv.drain(), csv.healing(), csv.critRate(), csv.ailmentChance(), csv.flinchChance(), csv.statChance());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvMoveMetaRepository.getAll().forEach(csvMoveMeta -> {
            if (csvMoveMeta.metaAilmentId() != null) {
                moveMetaRepository.findById(csvMoveMeta.metaAilmentId())
                        .ifPresent(moveMeta -> {
                            moveMetaRepository.linkMoveMetaToMoveAilment(moveMeta.getId(), csvMoveMeta.metaAilmentId());
                        });
            }
            if (csvMoveMeta.metaCategoryId() != null) {
                moveCategoryRepository.findById(csvMoveMeta.metaCategoryId())
                        .ifPresent(moveCategory -> moveMetaRepository.linkMoveMetaToMoveCategory(csvMoveMeta.metaCategoryId(), moveCategory.getId()));
            }
        });
    }
}
