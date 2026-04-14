package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvPokeathlonStatsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.PokeathlonStatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PokeathlonStatsService implements IPokemonDataLoader {

    private final CsvPokeathlonStatsRepositoryImpl csvMainRepo;
    private final PokeathlonStatsRepository neo4jRepo;


    public PokeathlonStatsService(CsvPokeathlonStatsRepositoryImpl csvMainRepo, 
                           PokeathlonStatsRepository neo4jRepo) {
        this.csvMainRepo = csvMainRepo;
        this.neo4jRepo = neo4jRepo;

    }

    @Override
    public String getEntityName() { return "PokeathlonStats"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvMainRepo.getAll().forEach(csv -> {
            neo4jRepo.insertPokeathlonStats(csv.getId(), csv.getIdentifier());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
