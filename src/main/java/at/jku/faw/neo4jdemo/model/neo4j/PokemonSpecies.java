package at.jku.faw.neo4jdemo.model.neo4j;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Getter
@Setter
@Node("PokemonSpecies")
public class PokemonSpecies {
	@Id
	public Long id;

	public String name;
	public String genus;
	// Physical & Gameplay Attributes
	public int genderRate;
	public int captureRate;
	public int baseHappiness;
	public boolean isBaby;
	public boolean isLegendary;
	public boolean isMythical;
	public int hatchCounter;
	public boolean hasGenderDifferences;
	public boolean formsSwitchable;
	public int order;
	public int conquestOrder;

	// --- RELATIONSHIPS ---

	// Points to the move FORWARD in the chain
	// The intuitive direction: Base -> Step -> Evolved
	@Relationship(type = "HAS_EVOLUTION_STEP")
	private List<EvolutionStep> evolutionSteps;

	// Points to the broad family container
	@Relationship(type = "PART_OF_CHAIN")
	private EvolutionChain evolutionChain;

	// Keep the back-reference for easy "Who was my predecessor?" queries
	@Relationship(type = "EVOLVES_FROM", direction = Relationship.Direction.INCOMING)
	private PokemonSpecies evolvesFrom;

	// (:PokemonSpecies)-[:HAS_COLOR]->(:Color)
	@Relationship(type = "HAS_COLOR")
	private PokemonColor color;

	// (:PokemonSpecies)-[:BELONGS_TO_GEN]->(:Generation)
	@Relationship(type = "BELONGS_TO_GEN")
	private Generation generation;

	// (:PokemonSpecies)-[:HAS_SHAPE]->(:Shape)
	@Relationship(type = "HAS_SHAPE")
	private PokemonShape shape;

	// (:PokemonSpecies)-[:LIVES_IN]->(:Habitat)
	@Relationship(type = "LIVES_IN")
	private PokemonHabitat habitat;

	// (:PokemonSpecies)-[:HAS_GROWTH_RATE]->(:GrowthRate)
	@Relationship(type = "HAS_GROWTH_RATE")
	private GrowthRate growthRate;

	@Relationship(type = "FOUND_IN_PAL_PARK")
	private List<PalParkEncounter> palParkEncounters;

	// (:PokemonSpecies)-[:HAS_POKEDEX {pokedexNr: num}]->(:Pokedex)
	@Relationship(type = "HAS_POKEDEX")
	private List<PokedexEntry> pokedexEntries;

	@Relationship(type = "BELONGS_TO_EGG_GROUP")
	private List<EggGroup> eggGroups;
}
