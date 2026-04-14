package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvMoveMetaAilmentsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.MoveAilmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MoveAilmentService implements IPokemonDataLoader {

    private final CsvMoveMetaAilmentsRepositoryImpl csvMoveMetaAilmentsRepository;
    private final MoveAilmentRepository moveAilmentRepository;

    public MoveAilmentService(CsvMoveMetaAilmentsRepositoryImpl csvMoveMetaAilmentsRepository,
                           MoveAilmentRepository moveAilmentRepository) {
        this.csvMoveMetaAilmentsRepository = csvMoveMetaAilmentsRepository;
        this.moveAilmentRepository = moveAilmentRepository;
    }

    @Override
    public String getEntityName() { return "MoveAilment"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvMoveMetaAilmentsRepository.getAll().forEach(csv -> {
            moveAilmentRepository.insertMoveAilment(csv.getId(), csv.getIdentifier(), csv.getName());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
