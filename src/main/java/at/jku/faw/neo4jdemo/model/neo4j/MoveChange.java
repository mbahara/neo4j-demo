package at.jku.faw.neo4jdemo.model.neo4j;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Setter
@Getter
@Node("MoveChange")
public class MoveChange {
	@Id
	private Long id;

	private int power;
	private int pp;
	private int accuracy;
	private int priority;
	private int effectChance;

	@Relationship(type = "CHANGED_IN")
	private VersionGroup versionGroup;

	@Relationship(type = "FOR_TYPE")
	private Type type;

	@Relationship(type = "HAS_EFFECT")
	private MoveEffect effect;

	@Relationship(type = "TARGETS")
	private MoveTarget target;
}
