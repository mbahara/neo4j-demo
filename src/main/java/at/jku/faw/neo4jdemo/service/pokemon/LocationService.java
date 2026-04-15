package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvLocationGameIndices;
import at.jku.faw.neo4jdemo.repository.csv.CsvLocationGameIndicesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvLocationRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.GameIndexRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.LocationRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocationService implements IPokemonDataLoader {

    private final CsvLocationRepositoryImpl csvLocationRepository;
    private final LocationRepository locationRepository;
    private final CsvLocationGameIndicesRepositoryImpl csvLocationGameIndicesRepositoryImpl;
    private final GameIndexRepository gameIndexRepository;

    public LocationService(CsvLocationRepositoryImpl csvLocationRepository,
                           LocationRepository neo4jRepo,
                           CsvLocationGameIndicesRepositoryImpl csvLocationGameIndicesRepositoryImpl,
                           GameIndexRepository gameIndexRepository) {
        this.csvLocationRepository = csvLocationRepository;
        this.locationRepository = neo4jRepo;
        this.csvLocationGameIndicesRepositoryImpl = csvLocationGameIndicesRepositoryImpl;
        this.gameIndexRepository = gameIndexRepository;
    }

    @Override
    public String getEntityName() { return "Location"; }

    @Override
    @Transactional
    public void loadNodes() {
        List<Map<String, Object>> rows = csvLocationRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("identifier", csv.getIdentifier());
                    row.put("name", csv.getName());
                    row.put("subtitle", csv.getSubtitle());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            locationRepository.batchInsertLocations(rows);
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        var gameIndicesMap = csvLocationGameIndicesRepositoryImpl.getAll().stream()
                        .collect(Collectors.groupingBy(CsvLocationGameIndices::getLocationId));

        csvLocationRepository.getAll().forEach(csv -> {
            if (csv.getRegionId() != null) {
                locationRepository.linkLocationToRegion(csv.getId(), csv.getRegionId());
            }
            List<CsvLocationGameIndices> csvGameIndices = gameIndicesMap.getOrDefault(csv.getId(), List.of());
            if (!csvGameIndices.isEmpty()) {
                csvGameIndices.forEach(csvGameIndex ->
                    gameIndexRepository.linkLocationHasGameIndex(csvGameIndex.getLocationId(), csvGameIndex.getGenerationId(), csvGameIndex.getGameIndex()));
            }
        });
    }
}
