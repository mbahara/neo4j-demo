package at.jku.faw.neo4jdemo.model.neo4j;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Setter
@Getter
@Node("Location")
public class Location {
	@Id
	private Long id;

	private String identifier;
	private String name;
	private String subtitle;

	// (:Location)-[:IN_REGION]->(:Region)
	@Relationship(type = "IN_REGION")
	private Region region;

	// (:Area)-[:PART_OF]->(:Location)
	@Relationship(type = "PART_OF", direction = Relationship.Direction.INCOMING)
	private List<Area> areas;

	// (:Location)-[:HAS_GAME_INDEX]->(:Generation)
	@Relationship(type = "HAS_GAME_INDEX")
	private List<GameIndex> gameIndices;
}
