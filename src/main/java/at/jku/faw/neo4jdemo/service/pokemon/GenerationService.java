package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvGenerationsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.GenerationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GenerationService implements IPokemonDataLoader {

    private final CsvGenerationsRepositoryImpl csvGenerationsRepository;
    private final GenerationRepository generationRepository;


    public GenerationService(CsvGenerationsRepositoryImpl csvGenerationsRepository,
                             GenerationRepository generationRepository) {
        this.csvGenerationsRepository = csvGenerationsRepository;
        this.generationRepository = generationRepository;
    }

    @Override
    public String getEntityName() { return "Generation"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvGenerationsRepository.getAll().forEach(csv -> {
            generationRepository.insertGeneration(csv.getId(), csv.getIdentifier(), csv.getName());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvGenerationsRepository.getAll().forEach(generation -> {
            if (generation.getMainRegionId() != null) {
                generationRepository.linkGenerationToRegion(generation.getId(), generation.getMainRegionId());
            }
        });
    }
}
