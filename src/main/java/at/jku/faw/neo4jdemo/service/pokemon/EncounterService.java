package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvLocationAreaEncounterRates;
import at.jku.faw.neo4jdemo.repository.csv.CsvEncountersRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvLocationAreaEncounterRatesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.EncounterRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        List<Map<String, Object>> rows = csvMainRepo.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("minLevel", csv.getMinLevel());
                    row.put("maxLevel", csv.getMaxLevel());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            Integer count = neo4jRepo.batchInsertEncounters(rows);
            System.out.println("Successfully loaded " + count + " Encounters nodes.");
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        Map<Long, List<CsvLocationAreaEncounterRates>> ratesMap =
                csvLocationAreaEncounterRatesRepositoryImpl.getAll().stream()
                        .collect(Collectors.groupingBy(CsvLocationAreaEncounterRates::getLocationAreaId));

        csvMainRepo.getAll().forEach(csv -> {
            Long encounterId = csv.getId();
            Long locationAreaId = csv.getLocationAreaId();

            if (csv.getVersionId() != null) {
                neo4jRepo.linkEncounterToVersion(encounterId, csv.getVersionId());
            }

            if (locationAreaId != null) {
                neo4jRepo.linkEncounterToLocationArea(encounterId, locationAreaId);

                List<CsvLocationAreaEncounterRates> rates = ratesMap.get(locationAreaId);
                if (rates != null) {
                    rates.forEach(rate ->
                            neo4jRepo.linkEncounterToEncounterMethod(encounterId, rate.getEncounterMethodId())
                    );
                }
            }
        });
    }
}
