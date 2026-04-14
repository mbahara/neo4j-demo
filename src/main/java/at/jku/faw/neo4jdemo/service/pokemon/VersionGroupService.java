package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvVersionGroupPokemonMoveMethodsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvVersionGroupRegionsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvVersionGroupRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.VersionGroupRepository;
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
        csvVersionGroupRepository.getAll().forEach(csv -> versionGroupRepository.insertVersionGroup(csv.getId(), csv.getIdentifier(), csv.getOrder()));
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvVersionGroupRepository.getAll().forEach(csv -> {
            if (csv.getGenerationId() != null) {
                versionGroupRepository.linkVersionGroupToGeneration(csv.getId(), csv.getGenerationId());
            }

            csvVersionGroupRegionsRepositoryImpl.getByVersionGroupId(csv.getId()).forEach(regions -> {
                if (regions.getRegionId() != null) {
                    versionGroupRepository.linkVersionGroupToRegion(csv.getId(), regions.getRegionId());
                }
            });

            csvVersionGroupPokemonMoveMethodsRepositoryImpl.getByVersionGroupId(csv.getId()).forEach(moveMethods -> versionGroupRepository.linkVersionGroupToMoveMethod(moveMethods.getVersionGroupId(), moveMethods.getPokemonMoveMethodId()));
        });
    }
}
