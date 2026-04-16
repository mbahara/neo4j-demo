package at.jku.faw.neo4jdemo.service.pokemon;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

@Service
public class PokemonLoaderService {
	private static final Logger log = LoggerFactory.getLogger(PokemonLoaderService.class);

	private final List<IPokemonDataLoader> pokemonDataLoaders;
	private final Neo4jClient neo4jClient;
	private final ResourceLoader resourceLoader;
	private volatile boolean isImporting = false;
	private volatile boolean isImported = false;

	public PokemonLoaderService(List<IPokemonDataLoader> allLoaders, Neo4jClient neo4jClient,
								ResourceLoader resourceLoader) {
		this.pokemonDataLoaders = allLoaders;
		this.neo4jClient = neo4jClient;
		this.resourceLoader = resourceLoader;
	}

	public void loadPokemonData() {
		if (isImporting) {
			log.warn("Import is already in progress. Ignoring request.");
			return;
		}

		if (isDatabasePopulated()) {
			log.info("--- Pokémon Data already exists in Neo4j. Skipping import. ---");
			return;
		}

		try {
			isImporting = true;
			Resource dumpResource = resourceLoader.getResource("classpath:data/pokemon.dump");

			if (dumpResource.exists()) {
				log.info("--- Dump file detected. Loading Pokémon graph... ---");
				try (InputStream is = dumpResource.getInputStream()) {
					String cypherDump = new String(is.readAllBytes(), StandardCharsets.UTF_8);

					neo4jClient.query(cypherDump).run();
					log.info("Pokémon Data restored from dump successfully.");
					return;
				} catch (IOException e) {
					log.error("Error reading dump file: {}. Falling back to CSV.", e.getMessage());
				}
			}

			log.info("--- No dump file found or failed to load. Starting standard CSV import... ---");
			executeImport();
		} catch (Exception e) {
			isImported = false;
		} finally {
			isImporting = false;
		}
	}

	private boolean isDatabasePopulated() {
		return neo4jClient.query("MATCH (n:Pokemon) RETURN count(n) > 0 AS exists")
				.fetchAs(Boolean.class)
				.one()
				.orElse(false) || isImported;
	}

	private void executeImport() {
		log.info("--- Creating Nodes ---");
		pokemonDataLoaders.forEach(IPokemonDataLoader::loadNodes);

		log.info("--- Creating Constraints and Indexes ---");
		createConstraintsAndIndexes();

		log.info("--- Creating Relationships ---");
		pokemonDataLoaders.forEach(IPokemonDataLoader::loadRelationships);

		isImported = true;
		log.info("Pokémon Data Import Finished Successfully.");
	}

	private void createConstraintsAndIndexes() {
		pokemonDataLoaders.forEach(loader -> {
			String label = loader.getEntityName();
			log.info("Creating uniqueness constraint for: {}", label);

			String constraintQuery = String.format(
					"CREATE CONSTRAINT %s_id_unique IF NOT EXISTS FOR (n:%s) REQUIRE n.id IS UNIQUE",
					label.toLowerCase(), label
			);

			neo4jClient.query(constraintQuery).run();
		});

		neo4jClient.query("CREATE INDEX pokemon_identifier_idx IF NOT EXISTS FOR (n:Pokemon) ON (n.identifier)").run();
		neo4jClient.query("CREATE INDEX species_name_idx IF NOT EXISTS FOR (n:PokemonSpecies) ON (n.name)").run();
	}
}
