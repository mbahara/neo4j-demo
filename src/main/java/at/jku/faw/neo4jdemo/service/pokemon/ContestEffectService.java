package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvContestEffectsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.ContestEffectRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContestEffectService implements IPokemonDataLoader {

    private final CsvContestEffectsRepositoryImpl csvContestEffectsRepository;
    private final ContestEffectRepository contestEffectRepository;

    public ContestEffectService(CsvContestEffectsRepositoryImpl csvContestEffectsRepository,
                           ContestEffectRepository neo4jRepo) {
        this.csvContestEffectsRepository = csvContestEffectsRepository;
        this.contestEffectRepository = neo4jRepo;
    }

    @Override
    public String getEntityName() { return "ContestEffect"; }

    @Override
    @Transactional
    public void loadNodes() {
        List<Map<String, Object>> rows = csvContestEffectsRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("appeal", csv.getAppeal());
                    row.put("jam", csv.getJam());
                    row.put("flavorText", csv.getFlavorText());
                    row.put("effect", csv.getEffect());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            Integer count = contestEffectRepository.batchInsertContestEffects(rows);
            System.out.println("Successfully loaded " + count + " ContestEffects nodes.");
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
