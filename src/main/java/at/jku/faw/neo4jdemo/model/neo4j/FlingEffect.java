package at.jku.faw.neo4jdemo.model.neo4j;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Getter
@Setter
@Node("FlingEffect")
public class FlingEffect {
	@Id
	public Long id;
	private String identifier;
	private String effect;
}
