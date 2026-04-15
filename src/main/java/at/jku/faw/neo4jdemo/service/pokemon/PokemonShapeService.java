package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonShapeRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.PokemonShapeRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PokemonShapeService implements IPokemonDataLoader {

    private final CsvPokemonShapeRepositoryImpl csvPokemonShapeRepository;
    private final PokemonShapeRepository pokemonShapeRepository;

    public PokemonShapeService(CsvPokemonShapeRepositoryImpl csvPokemonShapeRepository,
                           PokemonShapeRepository neo4jRepo) {
        this.csvPokemonShapeRepository = csvPokemonShapeRepository;
        this.pokemonShapeRepository = neo4jRepo;
    }

    @Override
    public String getEntityName() { return "PokemonShape"; }

    @Override
    @Transactional
    public void loadNodes() {
        List<Map<String, Object>> rows = csvPokemonShapeRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getPokemonShapeId());
                    row.put("name", csv.getName());
                    row.put("awesomeName", csv.getAwesomeName());
                    row.put("description", csv.getDescription());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            pokemonShapeRepository.batchInsertPokemonShapes(rows);
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
