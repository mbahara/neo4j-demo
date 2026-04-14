package at.jku.faw.neo4jdemo.model.neo4j;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Getter
@Setter
@Node("Region")
public class Region {
    @Id
    public Long id;
    private String identifier;

    @Relationship(type = "MAIN_REGION", direction = Relationship.Direction.INCOMING)
    private Generation generation;
}