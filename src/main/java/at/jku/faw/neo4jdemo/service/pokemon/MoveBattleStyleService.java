package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvMoveBattleStylesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.MoveBattleStyleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MoveBattleStyleService implements IPokemonDataLoader {

    private final CsvMoveBattleStylesRepositoryImpl csvMainRepo;
    private final MoveBattleStyleRepository neo4jRepo;


    public MoveBattleStyleService(CsvMoveBattleStylesRepositoryImpl csvMainRepo,
                           MoveBattleStyleRepository neo4jRepo) {
        this.csvMainRepo = csvMainRepo;
        this.neo4jRepo = neo4jRepo;
    }

    @Override
    public String getEntityName() { return "MoveBattleStyle"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvMainRepo.getAll().forEach(csv -> {
            neo4jRepo.insertMoveBattleStyle(csv.id(), csv.identifier(), csv.name());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
