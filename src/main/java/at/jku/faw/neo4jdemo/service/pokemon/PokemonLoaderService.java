package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.service.IDataLoader;
import java.util.List;
import org.neo4j.annotations.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.neo4j.core.Neo4jClient;

@Service
public class PokemonLoaderService {
	private static final Logger log = LoggerFactory.getLogger(PokemonLoaderService.class);

	private final List<IDataLoader> allLoaders;
	private final Neo4jClient neo4jClient;

	public PokemonLoaderService(List<IDataLoader> allLoaders, Neo4jClient neo4jClient) {
		this.allLoaders = allLoaders;
		this.neo4jClient = neo4jClient;
	}

	public void loadPokemonData() {
		log.info("--- Creating Nodes ---");
		allLoaders.forEach(IDataLoader::loadNodes);

		log.info("--- Creating Constraints and Indexes ---");
		createConstraintsAndIndexes();

		log.info("--- Creating Relationships ---");
		allLoaders.forEach(IDataLoader::loadRelationships);

		log.info("Pokémon Data Import Finished Successfully.");
	}

	private void createConstraintsAndIndexes() {
		allLoaders.forEach(loader -> {
			String label = loader.getEntityName();
			log.info("Creating uniqueness constraint for label: {}", label);

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
