package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvVersionGroupPokemonMoveMethodsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvVersionGroupRegionsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvVersionGroupRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.VersionGroupRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.VersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VersionGroupService implements IPokemonDataLoader {

    private final CsvVersionGroupRepositoryImpl csvVersionGroupRepository;
    private final VersionGroupRepository versionGroupRepository;
    private final CsvVersionGroupRegionsRepositoryImpl csvVersionGroupRegionsRepositoryImpl;
    private final CsvVersionGroupPokemonMoveMethodsRepositoryImpl csvVersionGroupPokemonMoveMethodsRepositoryImpl;
    private final VersionRepository versionRepository;

    public VersionGroupService(CsvVersionGroupRepositoryImpl csvVersionGroupRepository,
                               VersionGroupRepository versionGroupRepository,
                               CsvVersionGroupRegionsRepositoryImpl csvVersionGroupRegionsRepositoryImpl,
                               CsvVersionGroupPokemonMoveMethodsRepositoryImpl csvVersionGroupPokemonMoveMethodsRepositoryImpl,
                               VersionRepository versionRepository) {
        this.csvVersionGroupRepository = csvVersionGroupRepository;
        this.versionGroupRepository = versionGroupRepository;
        this.csvVersionGroupRegionsRepositoryImpl = csvVersionGroupRegionsRepositoryImpl;
        this.csvVersionGroupPokemonMoveMethodsRepositoryImpl = csvVersionGroupPokemonMoveMethodsRepositoryImpl;
        this.versionRepository = versionRepository;
    }

    @Override
    public String getEntityName() { return "VersionGroup"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvVersionGroupRepository.getAll().forEach(csv -> {
            versionGroupRepository.insertVersionGroup(csv.id(), csv.identifier(), csv.order());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvVersionGroupRepository.getAll().forEach(csv -> {
            if (csv.generationId() != null) {
                versionGroupRepository.linkVersionGroupToGeneration(csv.id(), csv.generationId());
            }

            csvVersionGroupRegionsRepositoryImpl.getByVersionGroupId(csv.id()).forEach(regions -> {
                if (regions.regionId() != null) {
                    versionGroupRepository.linkVersionGroupToRegion(csv.id(), regions.regionId());
                }
            });

            csvVersionGroupPokemonMoveMethodsRepositoryImpl.getByVersionGroupId(csv.id()).forEach(moveMethods -> {
               versionGroupRepository.linkVersionGroupToMoveMethod(moveMethods.versionGroupId(), moveMethods.pokemonMoveMethodId());
            });
        });
    }
}
