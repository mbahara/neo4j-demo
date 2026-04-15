package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvPokedexVersionGroups;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokedexVersionGroupsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokedexesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.PokedexRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PokedexService implements IPokemonDataLoader {

    private final CsvPokedexesRepositoryImpl csvPokedexesRepository;
    private final PokedexRepository pokedexRepository;
    private final CsvPokedexVersionGroupsRepositoryImpl csvPokedexVersionGroupsRepositoryImpl;

    public PokedexService(CsvPokedexesRepositoryImpl csvPokedexesRepository,
                          PokedexRepository pokedexRepository,
                          CsvPokedexVersionGroupsRepositoryImpl csvPokedexVersionGroupsRepositoryImpl) {
        this.csvPokedexesRepository = csvPokedexesRepository;
        this.pokedexRepository = pokedexRepository;
        this.csvPokedexVersionGroupsRepositoryImpl = csvPokedexVersionGroupsRepositoryImpl;
    }

    @Override
    public String getEntityName() { return "Pokedex"; }

    @Override
    @Transactional
    public void loadNodes() {
        List<Map<String, Object>> rows = csvPokedexesRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("identifier", csv.getIdentifier());
                    row.put("isMainSeries", csv.getIsMainSeries() != 0);
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            Integer count = pokedexRepository.batchInsertPokedexs(rows);
            System.out.println("Successfully loaded " + count + " Pokedexs nodes.");
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        Map<Long, List<CsvPokedexVersionGroups>> versionGroupMap =
                csvPokedexVersionGroupsRepositoryImpl.getAll().stream()
                        .collect(Collectors.groupingBy(CsvPokedexVersionGroups::getPokedexId));

        csvPokedexesRepository.getAll().forEach(csv -> {
            Long pokedexId = csv.getId();
            if (csv.getRegionId() != null) {
                pokedexRepository.linkPokedexToRegion(pokedexId, csv.getRegionId());
            }
            List<CsvPokedexVersionGroups> linkedGroups = versionGroupMap.get(pokedexId);
            if (linkedGroups != null) {
                linkedGroups.forEach(vg -> {
                    if (vg.getVersionGroupId() != null) {
                        pokedexRepository.linkPokedexToVersionGroup(pokedexId, vg.getVersionGroupId());
                    }
                });
            }
        });
    }
}
