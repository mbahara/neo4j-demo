package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonColorsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.PokemonColorRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PokemonColorService implements IPokemonDataLoader {

    private final CsvPokemonColorsRepositoryImpl csvPokemonColorsRepository;
    private final PokemonColorRepository pokemonColorRepository;

    public PokemonColorService(CsvPokemonColorsRepositoryImpl csvPokemonColorsRepository,
                           PokemonColorRepository pokemonColorRepository) {
        this.csvPokemonColorsRepository = csvPokemonColorsRepository;
        this.pokemonColorRepository = pokemonColorRepository;
    }

    @Override
    public String getEntityName() { return "PokemonColor"; }

    @Override
    @Transactional
    public void loadNodes() {
        List<Map<String, Object>> rows = csvPokemonColorsRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("identifier", csv.getIdentifier());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            pokemonColorRepository.batchInsertPokemonColors(rows);
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
