package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvRegionsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.RegionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegionService implements IPokemonDataLoader {

    private final CsvRegionsRepositoryImpl csvRegionsRepository;
    private final RegionRepository regionRepository;

    public RegionService(CsvRegionsRepositoryImpl csvRegionsRepository,
                         RegionRepository regionRepository) {
        this.csvRegionsRepository = csvRegionsRepository;
        this.regionRepository = regionRepository;
    }

    @Override
    public String getEntityName() { return "Region"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvRegionsRepository.getAll().forEach(csv -> {
            regionRepository.insertRegion(csv.getId(), csv.getIdentifier());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {

    }
}
