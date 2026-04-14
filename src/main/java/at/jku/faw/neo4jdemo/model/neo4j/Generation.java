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
@Node("Generation")
public class Generation {
    @Id
    public Long id;
    private String identifier;
    private String name;

    @Relationship(type = "MAIN_REGION")
    private Region mainRegion;

    @Relationship(type = "INTRODUCED_IN", direction = Relationship.Direction.INCOMING)
    private List<VersionGroup> versionGroups;
}