package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvEncounters;
import at.jku.faw.neo4jdemo.model.csv.CsvLocationAreaEncounterRates;
import at.jku.faw.neo4jdemo.repository.csv.CsvEncountersRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvLocationAreaEncounterRatesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.EncounterRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class EncounterService implements IPokemonDataLoader {

    private final CsvEncountersRepositoryImpl csvMainRepo;
    private final EncounterRepository neo4jRepo;
    private final CsvLocationAreaEncounterRatesRepositoryImpl csvLocationAreaEncounterRatesRepositoryImpl;
    private final PokemonBatchProcessor batchProcessor;

    public EncounterService(CsvEncountersRepositoryImpl csvMainRepo,
							EncounterRepository neo4jRepo,
							CsvLocationAreaEncounterRatesRepositoryImpl csvLocationAreaEncounterRatesRepositoryImpl,
							PokemonBatchProcessor batchProcessor) {
        this.csvMainRepo = csvMainRepo;
        this.neo4jRepo = neo4jRepo;
        this.csvLocationAreaEncounterRatesRepositoryImpl = csvLocationAreaEncounterRatesRepositoryImpl;
		this.batchProcessor = batchProcessor;
	}

    @Override
    public String getEntityName() { return "Encounter"; }

    @Override
    public void loadNodes() {
        List<CsvEncounters> allCsv = csvMainRepo.getAll();
        int total = allCsv.size();
        int chunkSize = 5000;

        for (int i = 0; i < allCsv.size(); i += chunkSize) {
            int end = Math.min(i + chunkSize, allCsv.size());
            List<Map<String, Object>> rows = allCsv.subList(i, end).stream()
                    .map(csv -> {
                        Map<String, Object> row = new HashMap<>();
                        row.put("id", csv.getId());
                        row.put("minLevel", csv.getMinLevel());
                        row.put("maxLevel", csv.getMaxLevel());
                        return row;
                    }).collect(Collectors.toList());

            batchProcessor.processNodeChunk(rows, neo4jRepo::batchInsertEncounters);
            int remaining = total - end;
            System.out.println("Processed: " + end + " | Remaining: " + remaining + " | Total: " + total);
        }

        System.out.println("Successfully loaded Encounters nodes.");
    }

    @Override
    public void loadRelationships() {
        Map<Long, List<CsvLocationAreaEncounterRates>> ratesMap =
                csvLocationAreaEncounterRatesRepositoryImpl.getAll().stream()
                        .collect(Collectors.groupingBy(CsvLocationAreaEncounterRates::getLocationAreaId));

        List<CsvEncounters> allEncounters = csvMainRepo.getAll();
        int total = allEncounters.size();
        int chunkSize = 5000;

        System.out.println("Linking Encounter relationships in chunks of " + chunkSize + "...");

        for (int i = 0; i < total; i += chunkSize) {
            int end = Math.min(i + chunkSize, total);
            List<CsvEncounters> chunk = allEncounters.subList(i, end);

            batchProcessor.processRelationshipChunk(() -> {
                for (CsvEncounters csv : chunk) {
                    Long encounterId = csv.getId();
                    Long locationAreaId = csv.getLocationAreaId();

                    // Link to Version
                    if (csv.getVersionId() != null) {
                        neo4jRepo.linkEncounterToVersion(encounterId, csv.getVersionId());
                    }

                    if (locationAreaId != null) {
                        // Link to Location Area
                        neo4jRepo.linkEncounterToLocationArea(encounterId, locationAreaId);

                        // Link to Encounter Methods
                        List<CsvLocationAreaEncounterRates> rates = ratesMap.get(locationAreaId);
                        if (rates != null) {
                            rates.forEach(rate ->
                                    neo4jRepo.linkEncounterToEncounterMethod(encounterId, rate.getEncounterMethodId())
                            );
                        }
                    }
                }
            });

            if (i % 5000 == 0 || end == total) {
                System.out.println("Encounter Relationship Progress: " + end + " / " + total);
            }
        }
    }
}
