package at.jku.faw.neo4jdemo.model.neo4j;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Node("Pokemon")
public class Pokemon {
	@Id
	public Long id;

	public String identifier;
	private int height;
	private int weight;
	private int baseExperience;
	private int order;
	private boolean isDefault;

	@Relationship(type = "HAS_STAT")
	private List<HasStats> stats; //pokemon_stats.csv

	@Relationship(type = "HOLDS_ITEM")
	private List<HeldItem> heldItems; //pokemon_items.csv

	@Relationship(type = "CAN_HAVE")
	private List<PokemonAbility> abilities; //pokemon_abilities.csv

	@Relationship(type = "BELONGS_TO_SPECIES")
	private PokemonSpecies species; // species_id from pokemon.csv

	@Relationship(type = "CAN_LEARN")
	private List<PokemonMove> moves; //pokemon_moves.csv

	// (:Pokemon)-[:FOUND_IN]->(:Area)
	@Relationship(type = "FOUND_IN")
	private List<Encounter> encounters; //encounters.csv

	@Relationship(type = "HAS_GAME_INDEX")
	private List<PokemonGameIndex> gameIndices; //pokemon_game_indices.csv

	@Relationship(type = "HAS_TYPE")
	private List<PokemonType> types; //pokemon_types.csv
}
