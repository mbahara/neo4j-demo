package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvPokemonHabitats;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonHabitatsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.PokemonHabitatRepository;
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
		for (CsvPokemonHabitats csv : csvPokemonHabitatsRepository.getAll()) {
			pokemonHabitatRepository.insertPokemonHabitat(csv.getId(), csv.getIdentifier());
		}
	}

    @Override
    @Transactional
    public void loadRelationships() {
        // no relationships
    }
}
