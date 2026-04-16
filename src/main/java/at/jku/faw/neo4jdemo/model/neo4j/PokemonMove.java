package at.jku.faw.neo4jdemo.model.neo4j;

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
@Node("PokemonMove")
public class PokemonMove {
	@Id
	private Long id;

	private int level;
	private int order;

	@Relationship(type = "MOVE_LEARNED")
	private Move move;

	@Relationship(type = "LEARNED_VIA")
	private MoveMethod method;

	@Relationship(type = "IN_VERSION_GROUP")
	private VersionGroup versionGroup;
}
