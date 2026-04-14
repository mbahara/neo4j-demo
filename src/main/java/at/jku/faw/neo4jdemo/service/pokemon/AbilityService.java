package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvAbilitiesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvAbilityChangelogRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvAbilityGenerationsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.AbilityRepository;
import java.util.Objects;
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
        csvAbilitiesRepository.getAll().forEach(csv -> {
            abilityRepository.insertAbility(csv.id(), csv.name(), csv.isMainSeries() != 0, csv.shortEffect(), csv.effect());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvAbilitiesRepository.getAll().forEach(csv -> {
            csvAbilityGenerationsRepositoryImpl.getAll().stream()
                    .filter(csvAbilityGeneration -> Objects.equals(csvAbilityGeneration.abilityId(), csv.id()))
                    .forEach(csvAbilityGeneration -> abilityRepository.linkAbilityToGeneration(csv.id(), csvAbilityGeneration.generationId()));
            csvAbilityChangelogRepositoryImpl.getAll().stream()
                    .filter(csvAbilityChangelog -> Objects.equals(csvAbilityChangelog.abilityId(), csv.id()))
                    .forEach(csvAbilityChangelog -> abilityRepository.linkAbilityToChangeEvent(csvAbilityChangelog.abilityId(), csvAbilityChangelog.id()));
        });
    }
}
