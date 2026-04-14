package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvPokemonMoves;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonMovesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.PokemonMoveRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PokemonMoveService implements IPokemonDataLoader {

    private final CsvPokemonMovesRepositoryImpl csvPokemonMovesRepository;
    private final PokemonMoveRepository pokemonMoveRepository;

    public PokemonMoveService(CsvPokemonMovesRepositoryImpl csvPokemonMovesRepository,
                              PokemonMoveRepository pokemonMoveRepository) {
        this.csvPokemonMovesRepository = csvPokemonMovesRepository;
        this.pokemonMoveRepository = pokemonMoveRepository;
    }

    @Override
    public String getEntityName() { return "PokemonMove"; }

    @Override
    @Transactional
    public void loadNodes() {
		for (CsvPokemonMoves csv : csvPokemonMovesRepository.getAll()) {
			pokemonMoveRepository.insertPokemonMove(csv.getLevel(), csv.getOrder());
		}
	}

    @Override
    @Transactional
    public void loadRelationships() {
		for (CsvPokemonMoves csvPokemonMoves : csvPokemonMovesRepository.getAll()) {
			pokemonMoveRepository.findAll().forEach(pokemonMove -> {
				pokemonMoveRepository.linkPokemonMoveToMove(pokemonMove.getId(), csvPokemonMoves.getMoveId());
				pokemonMoveRepository.linkPokemonMoveToMoveMethod(pokemonMove.getId(), csvPokemonMoves.getMoveMethodId());
				pokemonMoveRepository.linkPokemonMoveToVersionGroup(pokemonMove.getId(),
						csvPokemonMoves.getVersionGroupId());
			});
		}
	}
}
