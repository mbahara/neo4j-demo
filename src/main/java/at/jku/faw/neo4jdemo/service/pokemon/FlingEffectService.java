package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvItemFlingEffectsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.FlingEffectRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        List<Map<String, Object>> rows = csvItemFlingEffectsRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("identifier", csv.getIdentifier());
                    row.put("effect", csv.getEffect());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            flingEffectRepository.batchInsertFlingEffects(rows);
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
