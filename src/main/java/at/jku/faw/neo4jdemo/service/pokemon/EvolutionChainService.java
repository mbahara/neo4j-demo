package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvItem;
import at.jku.faw.neo4jdemo.repository.csv.CsvEvolutionChainsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvItemRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonSpeciesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.EvolutionChainRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        List<Map<String, Object>> rows = csvMainRepo.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            neo4jRepo.batchInsertEvolutionChains(rows);
        }
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
