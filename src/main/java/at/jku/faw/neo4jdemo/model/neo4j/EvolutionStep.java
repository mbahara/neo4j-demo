package at.jku.faw.neo4jdemo.model.neo4j;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Getter
@Setter
@Node("EvolutionStep")
public class EvolutionStep {
	@Id
	private Long id; // the ID from pokemon_evolution.csv

	private int minimumLevel;
	private int minimumHappiness;
	private String timeOfDay;
	private int relativePhysicalStats;
	private boolean needsOverworldRain;
	private int turnUpsideDown;
	private int minimumBeauty;
	private int minimumAffection;

	@Relationship(type = "TRIGGERED_BY")
	private EvolutionTrigger trigger;

	@Relationship(type = "REQUIRES")
	private Item triggerItem;

	@Relationship(type = "HAS_GENDER")
	private Gender gender;

	@Relationship(type = "HELD_ITEM")
	private Item heldItem;

	@Relationship(type = "KNOWN_MOVE")
	private Move knownMove;

	@Relationship(type = "AT_LOCATION")
	private Location location;

	@Relationship(type = "RESULTS_IN")
	private PokemonSpecies evolvedSpecies;
}
