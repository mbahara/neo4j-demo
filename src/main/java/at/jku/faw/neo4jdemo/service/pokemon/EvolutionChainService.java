package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvItem;
import at.jku.faw.neo4jdemo.repository.csv.CsvEvolutionChainsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvItemRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonSpeciesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.EvolutionChainRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EvolutionChainService implements IPokemonDataLoader {

    private final CsvEvolutionChainsRepositoryImpl csvMainRepo;
    private final EvolutionChainRepository neo4jRepo;
    private final CsvItemRepositoryImpl csvItemRepositoryImpl;
    private final CsvPokemonSpeciesRepositoryImpl csvPokemonSpeciesRepositoryImpl;

    public EvolutionChainService(CsvEvolutionChainsRepositoryImpl csvMainRepo,
                                 EvolutionChainRepository neo4jRepo, CsvItemRepositoryImpl csvItemRepositoryImpl,
                                 CsvPokemonSpeciesRepositoryImpl csvPokemonSpeciesRepositoryImpl) {
        this.csvMainRepo = csvMainRepo;
        this.neo4jRepo = neo4jRepo;
        this.csvItemRepositoryImpl = csvItemRepositoryImpl;
        this.csvPokemonSpeciesRepositoryImpl = csvPokemonSpeciesRepositoryImpl;
    }

    @Override
    public String getEntityName() { return "EvolutionChain"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvMainRepo.getAll().forEach(csv -> {
            neo4jRepo.insertEvolutionChain(csv.getId());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvMainRepo.getAll().forEach(csv -> {
            CsvItem item = csvItemRepositoryImpl.getById(csv.getBabyTriggerItemId());
            neo4jRepo.linkEvolutionChainToItem(csv.getId(), item.getId());
            csvPokemonSpeciesRepositoryImpl.getByEvolutionChainId(csv.getId()).forEach(
                    csvPokemonSpecies -> neo4jRepo.linkEvolutionChainToPokemonSpecies(csv.getId(), csvPokemonSpecies.getId())
            );
        });
    }
}
