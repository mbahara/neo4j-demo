package at.jku.faw.neo4jdemo.model.neo4j;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Node("Machine")
public class Machine {
	@Id
	@GeneratedValue
	private Long id;

	private int machineNumber;

	@Relationship(type = "TEACHES_MOVE")
	private Move move;

	@Relationship(type = "IN_VERSION_GROUP")
	private VersionGroup versionGroup;
}
