package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvPokemonMoves;
import at.jku.faw.neo4jdemo.model.neo4j.PokemonMove;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonMovesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.PokemonMoveRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class PokemonMoveService implements IPokemonDataLoader {

	private final CsvPokemonMovesRepositoryImpl csvPokemonMovesRepository;
	private final PokemonMoveRepository pokemonMoveRepository;
	private final PokemonBatchProcessor pokemonBatchProcessor;

	public PokemonMoveService(CsvPokemonMovesRepositoryImpl csvPokemonMovesRepository,
							  PokemonMoveRepository pokemonMoveRepository, PokemonBatchProcessor pokemonBatchProcessor) {
		this.csvPokemonMovesRepository = csvPokemonMovesRepository;
		this.pokemonMoveRepository = pokemonMoveRepository;
		this.pokemonBatchProcessor = pokemonBatchProcessor;
	}

	@Override
	public String getEntityName() { return "PokemonMove"; }

	@Override
	public void loadNodes() {
		List<CsvPokemonMoves> allCsv = csvPokemonMovesRepository.getAll();
		int total = allCsv.size();
		int chunkSize = 5000;

		System.out.println("Starting chunked load for " + total + " PokemonMove nodes...");

		for (int i = 0; i < total; i += chunkSize) {
			int end = Math.min(i + chunkSize, total);

			List<Map<String, Object>> rows = allCsv.subList(i, end).stream()
					.map(csv -> {
						Map<String, Object> row = new HashMap<>();
						row.put("level", csv.getLevel());
						row.put("order", csv.getOrder());
						return row;
					})
					.collect(Collectors.toList());

			pokemonBatchProcessor.processNodeChunk(rows, pokemonMoveRepository::batchInsertPokemonMoves);

			if (i % 25000 == 0) {
				System.out.println("Nodes Progress: " + i + "/" + total);
			}
		}
		//pokemonMoveRepository.createPokemonMoveIndex();
		System.out.println("Successfully loaded " + total + " PokemonMove nodes.");
	}

	@Override
	public void loadRelationships() {
		Map<Long, Long> moveNodeLookup = pokemonMoveRepository.findAll().stream()
				.filter(pm -> pm.getMove() != null)
				.collect(Collectors.toMap(
						pm -> pm.getMove().getId(),
						PokemonMove::getId,
						(existing, replacement) -> existing
				));

		List<at.jku.faw.neo4jdemo.model.csv.CsvPokemonMoves> allCsv = csvPokemonMovesRepository.getAll();
		int total = allCsv.size();
		int chunkSize = 2500;

		System.out.println("Linking PokemonMoves in chunks of " + chunkSize + "...");

		for (int i = 0; i < total; i += chunkSize) {
			int end = Math.min(i + chunkSize, total);
			List<at.jku.faw.neo4jdemo.model.csv.CsvPokemonMoves> chunk = allCsv.subList(i, end);

			pokemonBatchProcessor.processRelationshipChunk(() -> {
				for (at.jku.faw.neo4jdemo.model.csv.CsvPokemonMoves csv : chunk) {
					Long internalId = moveNodeLookup.get(csv.getMoveId());

					if (internalId != null) {
						pokemonMoveRepository.linkPokemonMoveToMove(internalId, csv.getMoveId());
						pokemonMoveRepository.linkPokemonMoveToMoveMethod(internalId, csv.getMoveMethodId());
						pokemonMoveRepository.linkPokemonMoveToVersionGroup(internalId, csv.getVersionGroupId());
					}
				}
			});

			if (i % 10000 == 0) {
				System.out.println("Relationship Progress: " + i + " / " + total);
			}
		}
		System.out.println("Successfully linked all PokemonMove relationships.");
	}
}