package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvVersionGroupPokemonMoveMethods;
import at.jku.faw.neo4jdemo.model.csv.CsvVersionGroupRegions;
import at.jku.faw.neo4jdemo.repository.csv.CsvVersionGroupPokemonMoveMethodsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvVersionGroupRegionsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvVersionGroupRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.VersionGroupRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VersionGroupService implements IPokemonDataLoader {

    private final CsvVersionGroupRepositoryImpl csvVersionGroupRepository;
    private final VersionGroupRepository versionGroupRepository;
    private final CsvVersionGroupRegionsRepositoryImpl csvVersionGroupRegionsRepositoryImpl;
    private final CsvVersionGroupPokemonMoveMethodsRepositoryImpl csvVersionGroupPokemonMoveMethodsRepositoryImpl;

    public VersionGroupService(CsvVersionGroupRepositoryImpl csvVersionGroupRepository,
                               VersionGroupRepository versionGroupRepository,
                               CsvVersionGroupRegionsRepositoryImpl csvVersionGroupRegionsRepositoryImpl,
                               CsvVersionGroupPokemonMoveMethodsRepositoryImpl csvVersionGroupPokemonMoveMethodsRepositoryImpl) {
        this.csvVersionGroupRepository = csvVersionGroupRepository;
        this.versionGroupRepository = versionGroupRepository;
        this.csvVersionGroupRegionsRepositoryImpl = csvVersionGroupRegionsRepositoryImpl;
        this.csvVersionGroupPokemonMoveMethodsRepositoryImpl = csvVersionGroupPokemonMoveMethodsRepositoryImpl;
    }

    @Override
    public String getEntityName() { return "VersionGroup"; }

    @Override
    @Transactional
    public void loadNodes() {
        List<Map<String, Object>> rows = csvVersionGroupRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("identifier", csv.getIdentifier());
                    row.put("order", csv.getOrder());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            Integer count = versionGroupRepository.batchInsertVersionGroups(rows);
            System.out.println("Successfully loaded " + count + " VersionGroups nodes.");
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        Map<Long, List<CsvVersionGroupRegions>> regionMap = csvVersionGroupRegionsRepositoryImpl.getAll().stream()
                .collect(Collectors.groupingBy(CsvVersionGroupRegions::getVersionGroupId));

        Map<Long, List<CsvVersionGroupPokemonMoveMethods>> moveMethodMap = csvVersionGroupPokemonMoveMethodsRepositoryImpl.getAll().stream()
                .collect(Collectors.groupingBy(CsvVersionGroupPokemonMoveMethods::getVersionGroupId));

        csvVersionGroupRepository.getAll().forEach(csv -> {
            Long vgId = csv.getId();
            if (csv.getGenerationId() != null) {
                versionGroupRepository.linkVersionGroupToGeneration(vgId, csv.getGenerationId());
            }

            List<CsvVersionGroupRegions> regions = regionMap.get(vgId);
            if (regions != null) {
                regions.forEach(r -> {
                    if (r.getRegionId() != null) {
                        versionGroupRepository.linkVersionGroupToRegion(vgId, r.getRegionId());
                    }
                });
            }

            List<CsvVersionGroupPokemonMoveMethods> moveMethods = moveMethodMap.get(vgId);
            if (moveMethods != null) {
                moveMethods.forEach(mm ->
                        versionGroupRepository.linkVersionGroupToMoveMethod(vgId, mm.getPokemonMoveMethodId())
                );
            }
        });
    }
}
