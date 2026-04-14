package at.jku.faw.neo4jdemo.model.neo4j;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Setter
@Getter
@Node("Encounter")
public class Encounter {
	@Id
	private Long id;

	private int minLevel;
	private int maxLevel;

	@Relationship(type = "IN_VERSION")
	private Version version;

	@Relationship(type = "VIA_METHOD")
	private EncounterMethod method;

	@Relationship(type = "AT_LOCATION_AREA")
	private Area locationArea;
}
