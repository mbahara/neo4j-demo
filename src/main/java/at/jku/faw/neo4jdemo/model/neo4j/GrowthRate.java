package at.jku.faw.neo4jdemo.model.neo4j;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Getter
@Setter
@Node("GrowthRate")
public class GrowthRate {
    @Id
    public Long id;

    private String identifier;
    private int formula;

    @Relationship(type = "REQUIRES_EXP")
    private List<LevelRequirement> levels;
}