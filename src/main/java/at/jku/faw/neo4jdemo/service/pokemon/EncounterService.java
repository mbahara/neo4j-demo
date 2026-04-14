package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvEncountersRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvLocationAreaEncounterRatesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.EncounterRepository;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EncounterService implements IPokemonDataLoader {

    private final CsvEncountersRepositoryImpl csvMainRepo;
    private final EncounterRepository neo4jRepo;
    private final CsvLocationAreaEncounterRatesRepositoryImpl csvLocationAreaEncounterRatesRepositoryImpl;

    public EncounterService(CsvEncountersRepositoryImpl csvMainRepo,
                            EncounterRepository neo4jRepo,
                            CsvLocationAreaEncounterRatesRepositoryImpl csvLocationAreaEncounterRatesRepositoryImpl) {
        this.csvMainRepo = csvMainRepo;
        this.neo4jRepo = neo4jRepo;
        this.csvLocationAreaEncounterRatesRepositoryImpl = csvLocationAreaEncounterRatesRepositoryImpl;
    }

    @Override
    public String getEntityName() { return "Encounter"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvMainRepo.getAll().forEach(csv -> {
            neo4jRepo.insertEncounter(csv.id(), csv.minLevel(), csv.maxLevel());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvMainRepo.getAll().forEach(csvEncounters -> {
            if (csvEncounters.versionId() != null) {
                neo4jRepo.linkEncounterToVersion(csvEncounters.id(), csvEncounters.versionId());
            }
            csvLocationAreaEncounterRatesRepositoryImpl.getAll().stream().filter(rates ->
                    Objects.equals(rates.locationAreaId(), csvEncounters.locationAreaId()))
                    .forEach(encounterRates ->
                            neo4jRepo.linkEncounterToEncounterMethod(csvEncounters.id(), encounterRates.encounterMethodId()));
            if (csvEncounters.locationAreaId() != null) {
                neo4jRepo.linkEncounterToLocationArea(csvEncounters.id(), csvEncounters.locationAreaId());
            }
        });
    }
}
