package at.jku.faw.neo4jdemo.model.neo4j;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Getter
@Setter
@RelationshipProperties
public class TypeEfficacy {
    @RelationshipId
    private Long id;

    // The 'damage_factor' column (e.g., 200 for Super Effective)
    private int damageFactor;

    @TargetNode
    private Type targetType;
}