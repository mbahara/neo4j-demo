package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvMoveTargetsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.MoveTargetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MoveTargetService implements IPokemonDataLoader {

    private final CsvMoveTargetsRepositoryImpl csvMoveTargetsRepository;
    private final MoveTargetRepository moveTargetRepository;


    public MoveTargetService(CsvMoveTargetsRepositoryImpl csvMoveTargetsRepository,
                             MoveTargetRepository moveTargetRepository) {
        this.csvMoveTargetsRepository = csvMoveTargetsRepository;
        this.moveTargetRepository = moveTargetRepository;

    }

    @Override
    public String getEntityName() { return "MoveTarget"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvMoveTargetsRepository.getAll().forEach(csv -> {
            moveTargetRepository.insertMoveTarget(csv.id(), csv.identifier(), csv.name(), csv.description());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
