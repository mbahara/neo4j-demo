package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvNatureBattleStylePreferencesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvNaturePokeathlonStatsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvNatureRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.NatureRepository;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NatureService implements IPokemonDataLoader {

    private final CsvNatureRepositoryImpl csvNatureRepository;
    private final NatureRepository natureRepository;
    private final CsvNaturePokeathlonStatsRepositoryImpl csvNaturePokeathlonStatsRepositoryImpl;
    private final CsvNatureBattleStylePreferencesRepositoryImpl csvNatureBattleStylePreferencesRepositoryImpl;

    public NatureService(CsvNatureRepositoryImpl csvNatureRepository,
                         NatureRepository natureRepository, CsvNaturePokeathlonStatsRepositoryImpl csvNaturePokeathlonStatsRepositoryImpl,
                         CsvNatureBattleStylePreferencesRepositoryImpl csvNatureBattleStylePreferencesRepositoryImpl) {
        this.csvNatureRepository = csvNatureRepository;
        this.natureRepository = natureRepository;
        this.csvNaturePokeathlonStatsRepositoryImpl = csvNaturePokeathlonStatsRepositoryImpl;
        this.csvNatureBattleStylePreferencesRepositoryImpl = csvNatureBattleStylePreferencesRepositoryImpl;
    }

    @Override
    public String getEntityName() { return "Nature"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvNatureRepository.getAll().forEach(csv -> {
            natureRepository.insertNature(csv.id(), csv.identifier());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvNatureRepository.getAll().forEach(csvNature -> {
            if (csvNature.increasedStatId() != null) {
                natureRepository.linkNatureIncreasesStat(csvNature.id(), csvNature.increasedStatId());
            }
            if (csvNature.decreasedStatId() != null) {
                natureRepository.linkNatureDecreasesStat(csvNature.id(), csvNature.decreasedStatId());
            }
            csvNatureBattleStylePreferencesRepositoryImpl.getByNatureId(csvNature.id()).forEach(csvNatureBattleStylePreference -> {
                natureRepository.linkNatureToMoveBattleStyle(csvNature.id(), csvNatureBattleStylePreference.moveBattleStyleId(), csvNatureBattleStylePreference.lowHpPreference(), csvNatureBattleStylePreference.highHpPreference());
            });

            csvNaturePokeathlonStatsRepositoryImpl.getAll().stream()
                .filter(stats -> Objects.equals(stats.natureId(), csvNature.id()))
                .forEach(stats ->
                        natureRepository.linkNatureToPokeathlonStats(stats.natureId(), stats.pokeathlonStatId(), stats.maxChange()));
        });
    }
}
