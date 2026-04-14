package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvNatureBattleStylePreferencesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvNaturePokeathlonStatsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvNatureRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.NatureBattleStylePreferenceRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.NatureRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.PokeathlonStatsModifierRepository;
import java.util.Objects;
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
        csvNatureRepository.getAll().forEach(csv -> {
            natureRepository.insertNature(csv.getId(), csv.getIdentifier());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvNatureRepository.getAll().forEach(csvNature -> {
            if (csvNature.getIncreasedStatId() != null) {
                natureRepository.linkNatureIncreasesStat(csvNature.getId(), csvNature.getIncreasedStatId());
            }
            if (csvNature.getDecreasedStatId() != null) {
                natureRepository.linkNatureDecreasesStat(csvNature.getId(), csvNature.getDecreasedStatId());
            }
            csvNatureBattleStylePreferencesRepositoryImpl.getByNatureId(csvNature.getId()).forEach(csvNatureBattleStylePreference -> {
                natureBattleStylePreferenceRepository.linkNatureToMoveBattleStyle(csvNature.getId(), csvNatureBattleStylePreference.getMoveBattleStyleId(), csvNatureBattleStylePreference.getLowHpPreference(), csvNatureBattleStylePreference.getHighHpPreference());
            });

            csvNaturePokeathlonStatsRepositoryImpl.getAll().stream()
                .filter(stats -> Objects.equals(stats.getNatureId(), csvNature.getId()))
                .forEach(stats ->
                        pokeathlonStatsModifierRepository.linkNatureToPokeathlonStats(stats.getNatureId(), stats.getPokeathlonStatId(), stats.getMaxChange()));
        });
    }
}
