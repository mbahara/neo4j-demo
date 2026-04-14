package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvItemFlingEffectsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.FlingEffectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FlingEffectService implements IPokemonDataLoader {

    private final CsvItemFlingEffectsRepositoryImpl csvItemFlingEffectsRepository;
    private final FlingEffectRepository flingEffectRepository;

    public FlingEffectService(CsvItemFlingEffectsRepositoryImpl csvItemFlingEffectsRepository,
                           FlingEffectRepository neo4jRepo) {
        this.csvItemFlingEffectsRepository = csvItemFlingEffectsRepository;
        this.flingEffectRepository = neo4jRepo;
    }

    @Override
    public String getEntityName() { return "FlingEffect"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvItemFlingEffectsRepository.getAll().forEach(csv -> {
            flingEffectRepository.insertFlingEffect(csv.id(), csv.identifier(), csv.effect());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
