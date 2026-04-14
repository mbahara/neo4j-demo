package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvPokedexVersionGroupsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokedexesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.PokedexRepository;
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
        csvPokedexesRepository.getAll().forEach(csv -> {
            pokedexRepository.insertPokedex(csv.getId(), csv.getIdentifier(), csv.getIsMainSeries() != 0);
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvPokedexesRepository.getAll().forEach(csv -> {
            if (csv.getRegionId() != null) {
                pokedexRepository.linkPokedexToRegion(csv.getId(), csv.getRegionId());
            }
            csvPokedexVersionGroupsRepositoryImpl.getByPokedexId(csv.getId()).forEach(csvVersionGroup -> {
                if (csvVersionGroup.getVersionGroupId() != null) {
                    pokedexRepository.linkPokedexToVersionGroup(csv.getId(), csvVersionGroup.getVersionGroupId());
                }
            });
        });
    }
}
