package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvItem;
import at.jku.faw.neo4jdemo.model.csv.CsvPokemonSpecies;
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
            Integer count = neo4jRepo.batchInsertEvolutionChains(rows);
            System.out.println("Successfully loaded " + count + " EvolutionChains nodes.");
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        Map<Long, CsvItem> itemMap = csvItemRepositoryImpl.getAll().stream()
                .collect(Collectors.toMap(CsvItem::getId, item -> item));

        Map<Long, List<CsvPokemonSpecies>> speciesMap = csvPokemonSpeciesRepositoryImpl.getAll().stream()
                .filter(s -> s.getEvolutionChainId() != null)
                .collect(Collectors.groupingBy(CsvPokemonSpecies::getEvolutionChainId));

        csvMainRepo.getAll().forEach(csv -> {
            Long chainId = csv.getId();

            if (csv.getBabyTriggerItemId() != null) {
                CsvItem item = itemMap.get(csv.getBabyTriggerItemId());
                if (item != null) {
                    neo4jRepo.linkEvolutionChainToItem(chainId, item.getId());
                }
            }

            List<CsvPokemonSpecies> linkedSpecies = speciesMap.get(chainId);
            if (linkedSpecies != null) {
                linkedSpecies.forEach(species ->
                        neo4jRepo.linkEvolutionChainToPokemonSpecies(chainId, species.getId())
                );
            }
        });
    }
}
