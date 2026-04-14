package at.jku.faw.neo4jdemo.model.neo4j;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Node("Item")
public class Item {
	@Id
	private Long id;
	private String identifier;
	private String name;
	private int cost;
	private int flingPower;
	private String shortEffect;
	private String effect;

	// (:Item)-[:BELONGS_TO]->(:ItemCategory)
	@Relationship(type = "BELONGS_TO")
	private ItemCategory category;

	// (:Item)-[:HAS_FLING_EFFECT]->(:FlingEffect)
	@Relationship(type = "HAS_FLING_EFFECT")
	private FlingEffect flingEffect;

	// (:Item)-[:HAS_FLAG]->(:ItemFlag)
	@Relationship(type = "HAS_FLAG")
	private List<ItemFlag> flags;

	// (:Item)-[:HAS_GAME_INDEX]->(:Generation)
	@Relationship(type = "HAS_GAME_INDEX")
	private List<GameIndex> gameIndices;

	@Relationship(type = "ACTS_AS_MACHINE")
	private List<Machine> machines;
}
