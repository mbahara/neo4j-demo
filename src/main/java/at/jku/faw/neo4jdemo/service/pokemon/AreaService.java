package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvLocationAreaEncounterRatesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvLocationAreasRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.AreaEncounterRateRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.AreaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AreaService implements IPokemonDataLoader {

    private final CsvLocationAreasRepositoryImpl csvLocationAreasRepository;
    private final AreaRepository areaRepository;
    private final CsvLocationAreaEncounterRatesRepositoryImpl csvLocationAreaEncounterRatesRepositoryImpl;
    private final AreaEncounterRateRepository areaEncounterRateRepository;

    public AreaService(CsvLocationAreasRepositoryImpl csvLocationAreasRepository,
                       AreaRepository areaRepository,
                       CsvLocationAreaEncounterRatesRepositoryImpl csvLocationAreaEncounterRatesRepositoryImpl,
                       AreaEncounterRateRepository areaEncounterRateRepository) {
        this.csvLocationAreasRepository = csvLocationAreasRepository;
        this.areaRepository = areaRepository;
        this.csvLocationAreaEncounterRatesRepositoryImpl = csvLocationAreaEncounterRatesRepositoryImpl;
        this.areaEncounterRateRepository = areaEncounterRateRepository;
    }

    @Override
    public String getEntityName() { return "Area"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvLocationAreasRepository.getAll().forEach(csv -> {
            areaRepository.insertArea(csv.id(), csv.identifier(), csv.name());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvLocationAreasRepository.getAll().forEach(locationArea -> {
            if (locationArea.locationId() != null) {
                areaRepository.linkAreaToLocation(locationArea.id(), locationArea.locationId());
            }

            csvLocationAreaEncounterRatesRepositoryImpl.getByLocationAreaId(locationArea.id()).forEach(
              encounterRates ->
                              areaEncounterRateRepository.linkAreaToEncounterMethod(encounterRates.locationAreaId(),
                                      encounterRates.encounterMethodId(), encounterRates.rate(),
                                      encounterRates.versionId())
            );
        });
    }
}
