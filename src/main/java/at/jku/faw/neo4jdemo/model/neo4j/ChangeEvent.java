package at.jku.faw.neo4jdemo.model.neo4j;

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
@Node("ChangeEvent")
public class ChangeEvent { // ability_changelog.csv
	@Id
	private Long id;
	private String effect;

	@Relationship(type = "OCCURRED_IN")
	private VersionGroup versionGroup;
}
