package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvPokemonMoves;
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

			final int chunkStartId = i + 1;
			final java.util.concurrent.atomic.AtomicInteger offset = new java.util.concurrent.atomic.AtomicInteger(0);

			List<Map<String, Object>> rows = allCsv.subList(i, end).stream()
					.map(csv -> {
						Map<String, Object> row = new HashMap<>();
						row.put("id", (long) chunkStartId + offset.getAndIncrement());
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

		pokemonMoveRepository.createPokemonMoveIndex();
		System.out.println("All PokemonMove nodes loaded and index created.");
	}

	@Override
	public void loadRelationships() {
		List<CsvPokemonMoves> allCsv = csvPokemonMovesRepository.getAll();
		int total = allCsv.size();
		int chunkSize = 2500;

		System.out.println("Linking PokemonMoves in chunks of " + chunkSize + "...");

		for (int i = 0; i < total; i += chunkSize) {
			int end = Math.min(i + chunkSize, total);
			List<CsvPokemonMoves> chunk = allCsv.subList(i, end);

			final long chunkStartId = i + 1;

			pokemonBatchProcessor.processRelationshipChunk(() -> {
				long currentIdOffset = 0;
				for (CsvPokemonMoves csv : chunk) {
					Long syntheticId = chunkStartId + currentIdOffset;
					currentIdOffset++;
					if (csv.getMoveId() != null) {
						pokemonMoveRepository.linkPokemonMoveToMove(syntheticId, csv.getMoveId());
					}
					if (csv.getMoveMethodId() != null) {
						pokemonMoveRepository.linkPokemonMoveToMoveMethod(syntheticId, csv.getMoveMethodId());
					}
					if (csv.getVersionGroupId() != null) {
						pokemonMoveRepository.linkPokemonMoveToVersionGroup(syntheticId, csv.getVersionGroupId());
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
