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
            neo4jRepo.insertEncounter(csv.getId(), csv.getMinLevel(), csv.getMaxLevel());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvMainRepo.getAll().forEach(csvEncounters -> {
            if (csvEncounters.getVersionId() != null) {
                neo4jRepo.linkEncounterToVersion(csvEncounters.getId(), csvEncounters.getVersionId());
            }
            csvLocationAreaEncounterRatesRepositoryImpl.getAll().stream().filter(rates ->
                    Objects.equals(rates.getLocationAreaId(), csvEncounters.getLocationAreaId()))
                    .forEach(encounterRates ->
                            neo4jRepo.linkEncounterToEncounterMethod(csvEncounters.getId(), encounterRates.getEncounterMethodId()));
            if (csvEncounters.getLocationAreaId() != null) {
                neo4jRepo.linkEncounterToLocationArea(csvEncounters.getId(), csvEncounters.getLocationAreaId());
            }
        });
    }
}
