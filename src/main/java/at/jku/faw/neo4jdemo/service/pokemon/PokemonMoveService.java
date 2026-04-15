package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.neo4j.PokemonMove;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonMovesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.PokemonMoveRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
		List<Map<String, Object>> rows = csvPokemonMovesRepository.getAll().stream()
				.map(csvPokemonMoves -> {
					Map<String, Object> row = new HashMap<>();
					row.put("level", csvPokemonMoves.getLevel());
					row.put("order", csvPokemonMoves.getOrder());
					return row;
				})
				.collect(Collectors.toList());

		if (!rows.isEmpty()) {
			pokemonMoveRepository.batchInsertPokemonMoves(rows);
		}
	}

    @Override
    @Transactional
    public void loadRelationships() {
		Map<Long, Long> moveNodeLookup = pokemonMoveRepository.findAll().stream()
				.filter(pm -> pm.getMove() != null)
				.collect(Collectors.toMap(
						pm -> pm.getMove().getId(),
						PokemonMove::getId,
						(existing, replacement) -> existing
				));

		csvPokemonMovesRepository.getAll().forEach(csv -> {
			Long internalId = moveNodeLookup.get(csv.getMoveId());

			if (internalId != null) {
				pokemonMoveRepository.linkPokemonMoveToMove(internalId, csv.getMoveId());
				pokemonMoveRepository.linkPokemonMoveToMoveMethod(internalId, csv.getMoveMethodId());
				pokemonMoveRepository.linkPokemonMoveToVersionGroup(internalId, csv.getVersionGroupId());
			}
		});
	}
}
