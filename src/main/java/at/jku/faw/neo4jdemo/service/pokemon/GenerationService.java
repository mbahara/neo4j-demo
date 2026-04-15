package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvGenerationsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.GenerationRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        List<Map<String, Object>> rows = csvGenerationsRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("identifier", csv.getIdentifier());
                    row.put("name", csv.getName());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            Integer count = generationRepository.batchInsertGenerations(rows);
            System.out.println("Successfully loaded " + count + " Generations nodes.");
        }
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
