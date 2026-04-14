package at.jku.faw.neo4jdemo.model.neo4j;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RelationshipProperties
public class PokeathlonStatsModifier {
	@Id
	@GeneratedValue
	private Long id;

	private int maxChange;

	@TargetNode
	private PokeathlonStats stat;
}
