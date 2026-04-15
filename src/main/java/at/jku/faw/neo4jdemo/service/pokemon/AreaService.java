package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvLocationAreaEncounterRates;
import at.jku.faw.neo4jdemo.repository.csv.CsvLocationAreaEncounterRatesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvLocationAreasRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.AreaEncounterRateRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.AreaRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        List<Map<String, Object>> rows = csvLocationAreasRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("identifier", csv.getIdentifier());
                    row.put("name", csv.getName());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            areaRepository.batchInsertAreas(rows);
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        var rates = csvLocationAreaEncounterRatesRepositoryImpl.getAll().stream().collect(Collectors.groupingBy(
                CsvLocationAreaEncounterRates::getLocationAreaId));


        csvLocationAreasRepository.getAll().forEach(locationArea -> {
            if (locationArea.getLocationId() != null) {
                areaRepository.linkAreaToLocation(locationArea.getId(), locationArea.getLocationId());
            }
            List<CsvLocationAreaEncounterRates> encounterRates = rates.get(locationArea.getId());
            if (encounterRates != null) {
                encounterRates.forEach(encounterRate -> {
                    areaEncounterRateRepository.linkAreaToEncounterMethod(encounterRate.getLocationAreaId(),
                            encounterRate.getEncounterMethodId(), encounterRate.getRate(),
                            encounterRate.getVersionId());
                });
            }
        });
    }
}
