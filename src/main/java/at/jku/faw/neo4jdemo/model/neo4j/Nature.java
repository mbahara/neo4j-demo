package at.jku.faw.neo4jdemo.model.neo4j;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.internal.batchimport.stats.Stat;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Setter
@Getter
@Node("Nature")
public class Nature {
	@Id
	private Long id;
	private String identifier;
	private String name;

	// (:Nature)-[:INCREASES]->(:Stat)
	@Relationship(type = "INCREASES")
	private Stat increasedStat;

	// (:Nature)-[:DECREASES]->(:Stat)
	@Relationship(type = "DECREASES")
	private Stat decreasedStat;

	// Preferences for the Battle Style
	@Relationship(type = "PREFERRED_BATTLE_STYLE")
	private List<NatureBattleStylePreference> battlePreferences;

	// Performance in the Pokeathlon
	@Relationship(type = "POKEATHLON_MODIFIER")
	private List<PokeathlonStatsModifier> pokeathlonStats;
}
