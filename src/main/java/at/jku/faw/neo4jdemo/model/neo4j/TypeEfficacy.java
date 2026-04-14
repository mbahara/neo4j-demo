package at.jku.faw.neo4jdemo.model.neo4j;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RelationshipProperties
public class TypeEfficacy {
    @Id
    @GeneratedValue
    private String id;

    // The 'damage_factor' column (e.g., 200 for Super Effective)
    @Property("damageFactor")
    private int damageFactor;

    @TargetNode
    private Type targetType;
}