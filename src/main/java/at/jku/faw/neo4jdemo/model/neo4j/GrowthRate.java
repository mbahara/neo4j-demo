package at.jku.faw.neo4jdemo.model.neo4j;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Node("GrowthRate")
public class GrowthRate {
    @Id
    public Long id;

    private String identifier;
    private String formula;

    @Relationship(type = "REQUIRES_EXP")
    private List<LevelRequirement> levels;
}