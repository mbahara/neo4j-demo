package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvNatureBattleStylePreferences;
import at.jku.faw.neo4jdemo.model.csv.CsvNaturePokeathlonStats;
import at.jku.faw.neo4jdemo.repository.csv.CsvNatureBattleStylePreferencesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvNaturePokeathlonStatsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvNatureRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.NatureBattleStylePreferenceRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.NatureRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.PokeathlonStatsModifierRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NatureService implements IPokemonDataLoader {

    private final CsvNatureRepositoryImpl csvNatureRepository;
    private final NatureRepository natureRepository;
    private final CsvNaturePokeathlonStatsRepositoryImpl csvNaturePokeathlonStatsRepositoryImpl;
    private final CsvNatureBattleStylePreferencesRepositoryImpl csvNatureBattleStylePreferencesRepositoryImpl;
    private final NatureBattleStylePreferenceRepository natureBattleStylePreferenceRepository;
    private final PokeathlonStatsModifierRepository pokeathlonStatsModifierRepository;

    public NatureService(CsvNatureRepositoryImpl csvNatureRepository,
                         NatureRepository natureRepository, CsvNaturePokeathlonStatsRepositoryImpl csvNaturePokeathlonStatsRepositoryImpl,
                         CsvNatureBattleStylePreferencesRepositoryImpl csvNatureBattleStylePreferencesRepositoryImpl,
                         NatureBattleStylePreferenceRepository natureBattleStylePreferenceRepository,
                         PokeathlonStatsModifierRepository pokeathlonStatsModifierRepository) {
        this.csvNatureRepository = csvNatureRepository;
        this.natureRepository = natureRepository;
        this.csvNaturePokeathlonStatsRepositoryImpl = csvNaturePokeathlonStatsRepositoryImpl;
        this.csvNatureBattleStylePreferencesRepositoryImpl = csvNatureBattleStylePreferencesRepositoryImpl;
        this.natureBattleStylePreferenceRepository = natureBattleStylePreferenceRepository;
        this.pokeathlonStatsModifierRepository = pokeathlonStatsModifierRepository;
    }

    @Override
    public String getEntityName() { return "Nature"; }

    @Override
    @Transactional
    public void loadNodes() {
        List<Map<String, Object>> rows = csvNatureRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("identifier", csv.getIdentifier());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            Integer count = natureRepository.batchInsertNatures(rows);
            System.out.println("Successfully loaded " + count + " Natures nodes.");
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        Map<Long, List<CsvNatureBattleStylePreferences>> preferenceMap =
                csvNatureBattleStylePreferencesRepositoryImpl.getAll().stream()
                        .collect(Collectors.groupingBy(CsvNatureBattleStylePreferences::getNatureId));

        Map<Long, List<CsvNaturePokeathlonStats>> pokeathlonMap =
                csvNaturePokeathlonStatsRepositoryImpl.getAll().stream()
                        .collect(Collectors.groupingBy(CsvNaturePokeathlonStats::getNatureId));

        csvNatureRepository.getAll().forEach(csvNature -> {
            Long natureId = csvNature.getId();
            if (csvNature.getIncreasedStatId() != null) {
                natureRepository.linkNatureIncreasesStat(natureId, csvNature.getIncreasedStatId());
            }
            if (csvNature.getDecreasedStatId() != null) {
                natureRepository.linkNatureDecreasesStat(natureId, csvNature.getDecreasedStatId());
            }

            List<CsvNatureBattleStylePreferences> prefs = preferenceMap.get(natureId);
            if (prefs != null) {
                prefs.forEach(p -> natureBattleStylePreferenceRepository.linkNatureToMoveBattleStyle(
                        natureId, p.getMoveBattleStyleId(), p.getLowHpPreference(), p.getHighHpPreference()));
            }

            List<CsvNaturePokeathlonStats> stats = pokeathlonMap.get(natureId);
            if (stats != null) {
                stats.forEach(s -> pokeathlonStatsModifierRepository.linkNatureToPokeathlonStats(
                        natureId, s.getPokeathlonStatId(), s.getMaxChange()));
            }
        });
    }
}
