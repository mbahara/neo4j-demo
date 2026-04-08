package at.jku.faw.neo4jdemo.model.neo4j;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Setter
@Getter
@RelationshipProperties
public class HasStats {
	@RelationshipId @GeneratedValue
	private Long id;

	private Integer baseStat; // from 'base_stat'
	private Integer effort;   // from 'effort'

	@TargetNode
	private Stats stat;
}
