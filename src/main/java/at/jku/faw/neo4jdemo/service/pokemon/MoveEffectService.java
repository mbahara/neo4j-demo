package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvMoveEffectRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.MoveEffectRepository;
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
        csvMoveEffectRepository.getAll().forEach(csv -> {
            moveEffectRepository.insertMoveEffect(csv.getId(), csv.getShortEffect(), csv.getEffect());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
