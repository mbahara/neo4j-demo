package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvEvolutionTriggersRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.EvolutionTriggerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EvolutionTriggerService implements IPokemonDataLoader {

    private final CsvEvolutionTriggersRepositoryImpl csvMainRepo;
    private final EvolutionTriggerRepository neo4jRepo;


    public EvolutionTriggerService(CsvEvolutionTriggersRepositoryImpl csvMainRepo,
                           EvolutionTriggerRepository neo4jRepo) {
        this.csvMainRepo = csvMainRepo;
        this.neo4jRepo = neo4jRepo;
    }

    @Override
    public String getEntityName() { return "EvolutionTrigger"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvMainRepo.getAll().forEach(csv -> {
            neo4jRepo.insertEvolutionTrigger(csv.id(), csv.identifier());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
