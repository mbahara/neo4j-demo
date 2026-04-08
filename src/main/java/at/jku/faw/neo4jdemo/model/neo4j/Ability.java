package at.jku.faw.neo4jdemo.model.neo4j;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Getter
@Setter
@Node("Ability")
public class Ability {
	@Id
	private Long id;
	private int name;
	private int isMainSeries;
	private int shortEffect;
	private int effect;

	@Relationship(type = "INTRODUCED_IN")
	private Generation generation; // ability_generations.csv

	@Relationship(type = "HAS_CHANGELOG")
	private Set<ChangeEvent> changeEvents;
}
