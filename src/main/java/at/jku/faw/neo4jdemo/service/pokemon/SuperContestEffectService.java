package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvSuperContestEffectsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.SuperContestEffectRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SuperContestEffectService implements IPokemonDataLoader {

    private final CsvSuperContestEffectsRepositoryImpl csvSuperContestEffectsRepository;
    private final SuperContestEffectRepository superContestEffectRepository;

    public SuperContestEffectService(CsvSuperContestEffectsRepositoryImpl csvSuperContestEffectsRepository,
                                     SuperContestEffectRepository superContestEffectRepository) {
        this.csvSuperContestEffectsRepository = csvSuperContestEffectsRepository;
        this.superContestEffectRepository = superContestEffectRepository;
    }

    @Override
    public String getEntityName() { return "SuperContestEffect"; }

    @Override
    @Transactional
    public void loadNodes() {
        List<Map<String, Object>> rows = csvSuperContestEffectsRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("appeal", csv.getAppeal());
                    row.put("flavorText", csv.getFlavorText());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            Integer count = superContestEffectRepository.batchInsertSuperContestEffects(rows);
            System.out.println("Successfully loaded " + count + " SuperContestEffects nodes.");
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        //
    }
}
