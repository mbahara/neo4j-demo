package at.jku.faw.neo4jdemo.model.neo4j;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Getter
@Setter
@Node("Characteristic")
public class Characteristic {
    @Id
    public Long id;
    private String description;

    @Relationship(type = "HIGHLIGHTS", direction = Relationship.Direction.OUTGOING)
    private List<Highlights> highlights;
}