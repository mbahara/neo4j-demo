package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvLocationAreaEncounterRatesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvLocationAreasRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.AreaRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.EncounterMethodRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AreaService implements IPokemonDataLoader {

    private final CsvLocationAreasRepositoryImpl csvLocationAreasRepository;
    private final AreaRepository areaRepository;
    private final CsvLocationAreaEncounterRatesRepositoryImpl csvLocationAreaEncounterRatesRepositoryImpl;

    public AreaService(CsvLocationAreasRepositoryImpl csvLocationAreasRepository,
                       AreaRepository areaRepository,
                       CsvLocationAreaEncounterRatesRepositoryImpl csvLocationAreaEncounterRatesRepositoryImpl,
                       EncounterMethodRepository encounterMethodRepository) {
        this.csvLocationAreasRepository = csvLocationAreasRepository;
        this.areaRepository = areaRepository;
        this.csvLocationAreaEncounterRatesRepositoryImpl = csvLocationAreaEncounterRatesRepositoryImpl;
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
                              areaRepository.linkAreaToEncounterMethod(encounterRates.locationAreaId(),
                                      encounterRates.encounterMethodId(), encounterRates.rate(),
                                      encounterRates.versionId())
            );
        });
    }
}
