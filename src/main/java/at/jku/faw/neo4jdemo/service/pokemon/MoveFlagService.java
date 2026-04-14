package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvMoveFlagsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.MoveFlagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MoveFlagService implements IPokemonDataLoader {

    private final CsvMoveFlagsRepositoryImpl csvMoveFlagsRepository;
    private final MoveFlagRepository moveFlagRepository;


    public MoveFlagService(CsvMoveFlagsRepositoryImpl csvMoveFlagsRepository,
                           MoveFlagRepository neo4jRepo) {
        this.csvMoveFlagsRepository = csvMoveFlagsRepository;
        this.moveFlagRepository = neo4jRepo;
    }

    @Override
    public String getEntityName() { return "MoveFlag"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvMoveFlagsRepository.getAll().forEach(csv -> {
            moveFlagRepository.insertMoveFlag(csv.id(), csv.identifier());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
