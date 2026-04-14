package at.jku.faw.neo4jdemo.model.neo4j;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Node("EncounterMethod")
public class EncounterMethod {
	@Id
	private Long id;

	private String identifier;
	private String name;
	private int order;
}
