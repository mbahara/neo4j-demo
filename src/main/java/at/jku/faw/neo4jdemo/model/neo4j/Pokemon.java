package at.jku.faw.neo4jdemo.model.neo4j;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Getter
@Setter
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
	private List<HasStats> stats;

	@Relationship(type = "HOLDS_ITEM")
	private List<HeldItem> heldItems;

	@Relationship(type = "CAN_HAVE")
	private List<PokemonAbility> abilities;

	@Relationship(type = "BELONGS_TO_SPECIES")
	private PokemonSpecies species;

	@Relationship(type = "CAN_LEARN")
	private List<PokemonMove> moves;

	// (:Pokemon)-[:FOUND_IN]->(:Area)
	@Relationship(type = "FOUND_IN")
	private List<Encounter> encounters;

	@Relationship(type = "HAS_GAME_INDEX")
	private List<PokemonGameIndex> gameIndices;

	@Relationship(type = "HAS_TYPE")
	private List<PokemonType> types;
}
