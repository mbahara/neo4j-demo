package at.jku.faw.neo4jdemo.model.neo4j;

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
@Node("Version")
public class Version {
    @Id
    public Long id;

    private String identifier;
    private String name;

    // (:Version)-[:PART_OF_GROUP]->(:VersionGroup)
    @Relationship(type = "PART_OF_GROUP")
    private VersionGroup versionGroup;
}