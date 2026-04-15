package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvAbilityChangelog;
import at.jku.faw.neo4jdemo.model.csv.CsvAbilityGenerations;
import at.jku.faw.neo4jdemo.repository.csv.CsvAbilitiesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvAbilityChangelogRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvAbilityGenerationsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.AbilityRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AbilityService implements IPokemonDataLoader {

    private final CsvAbilitiesRepositoryImpl csvAbilitiesRepository;
    private final AbilityRepository abilityRepository;
    private final CsvAbilityGenerationsRepositoryImpl csvAbilityGenerationsRepositoryImpl;
    private final CsvAbilityChangelogRepositoryImpl csvAbilityChangelogRepositoryImpl;


    public AbilityService(CsvAbilitiesRepositoryImpl csvAbilitiesRepository,
                          AbilityRepository abilityRepository,
                          CsvAbilityGenerationsRepositoryImpl csvAbilityGenerationsRepositoryImpl,
                          CsvAbilityChangelogRepositoryImpl csvAbilityChangelogRepositoryImpl) {
        this.csvAbilitiesRepository = csvAbilitiesRepository;
        this.abilityRepository = abilityRepository;
        this.csvAbilityGenerationsRepositoryImpl = csvAbilityGenerationsRepositoryImpl;
        this.csvAbilityChangelogRepositoryImpl = csvAbilityChangelogRepositoryImpl;
    }

    @Override
    public String getEntityName() { return "Ability"; }

    @Override
    @Transactional
    public void loadNodes() {
        List<Map<String, Object>> rows = csvAbilitiesRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("name", csv.getName());
                    row.put("isMainSeries", csv.getIsMainSeries() != 0);
                    row.put("shortEffect", csv.getShortEffect());
                    row.put("effect", csv.getEffect());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            Integer count = abilityRepository.batchInsertAbilities(rows);
            System.out.println("Successfully loaded " + count + " Abilities nodes.");
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        var genMap = csvAbilityGenerationsRepositoryImpl.getAll().stream()
                .collect(Collectors.groupingBy(CsvAbilityGenerations::getAbilityId));
        var changelogMap = csvAbilityChangelogRepositoryImpl.getAll().stream()
                .collect(Collectors.groupingBy(CsvAbilityChangelog::getAbilityId));

        csvAbilitiesRepository.getAll().forEach(csv -> {
            List<CsvAbilityGenerations> linkedGens = genMap.get(csv.getId());
            if (linkedGens != null) {
                linkedGens.forEach(gen ->
                        abilityRepository.linkAbilityToGeneration(csv.getId(), gen.getGenerationId())
                );
            }
            List<CsvAbilityChangelog> linkedChangelogs = changelogMap.get(csv.getId());
            if (linkedChangelogs != null) {
                linkedChangelogs.forEach(changelog ->
                        abilityRepository.linkAbilityToChangeEvent(changelog.getAbilityId(), changelog.getId())
                );
            }
        });
    }
}
