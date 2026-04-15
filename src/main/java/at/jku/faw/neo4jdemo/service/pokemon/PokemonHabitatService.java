package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonHabitatsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.PokemonHabitatRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PokemonHabitatService implements IPokemonDataLoader {

    private final CsvPokemonHabitatsRepositoryImpl csvPokemonHabitatsRepository;
    private final PokemonHabitatRepository pokemonHabitatRepository;


    public PokemonHabitatService(CsvPokemonHabitatsRepositoryImpl csvPokemonHabitatsRepository,
                           PokemonHabitatRepository neo4jRepo) {
        this.csvPokemonHabitatsRepository = csvPokemonHabitatsRepository;
        this.pokemonHabitatRepository = neo4jRepo;
    }

    @Override
    public String getEntityName() { return "PokemonHabitat"; }

    @Override
    @Transactional
    public void loadNodes() {
		List<Map<String, Object>> rows = csvPokemonHabitatsRepository.getAll().stream()
				.map(csvPokemonHabitats -> {
					Map<String, Object> row = new HashMap<>();
						row.put("id", csvPokemonHabitats.getId());
						row.put("identifier", csvPokemonHabitats.getIdentifier());
						return row;
				})
				.collect(Collectors.toList());

		if (!rows.isEmpty()) {
			Integer count = pokemonHabitatRepository.batchInsertPokemonHabitats(rows);
			System.out.println("Successfully loaded " + count + " PokemonHabitats nodes.");
		}
	}

    @Override
    @Transactional
    public void loadRelationships() {
        // no relationships
    }
}
