package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvRegionsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.RegionRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        List<Map<String, Object>> rows = csvRegionsRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("identifier", csv.getIdentifier());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            Integer count = regionRepository.batchInsertRegions(rows);
            System.out.println("Successfully loaded " + count + " Regions nodes.");
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {

    }
}
