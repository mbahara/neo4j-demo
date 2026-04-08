package at.jku.faw.neo4jdemo.model.neo4j;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Getter
@Setter
@Node("SuperContestEffect")
public class SuperContestEffect {
    @Id
    public Long id;

    private int appeal;
    private String flavorText;

    // (:Move)-[:HAS_SUPER_CONTEST_EFFECT]->(:SuperContestEffect)
    @Relationship(type = "HAS_SUPER_CONTEST_EFFECT", direction = Relationship.Direction.INCOMING)
    private List<Move> moves;
}