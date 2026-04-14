package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvLocationGameIndicesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvLocationRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.GameIndexRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.LocationRepository;
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
        csvLocationRepository.getAll().forEach(csv -> {
            locationRepository.insertLocation(csv.id(), csv.identifier(), csv.name(), csv.subtitle());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvLocationRepository.getAll().forEach(csv -> {
            if (csv.regionId() != null) {
                locationRepository.linkLocationToRegion(csv.id(), csv.regionId());
            }

            csvLocationGameIndicesRepositoryImpl.getByLocationId(csv.id()).forEach(csvGameIndex -> {
                gameIndexRepository.linkLocationHasGameIndex(csvGameIndex.locationId(), csvGameIndex.generationId(), csvGameIndex.gameIndex());
            });
        });
    }
}
