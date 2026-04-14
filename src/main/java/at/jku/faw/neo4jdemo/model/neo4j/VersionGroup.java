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
@Node("VersionGroup")
public class VersionGroup {
    @Id
    private Long id;

    private String identifier;
    private int order;

    @Relationship(type = "INTRODUCED_IN")
    private Generation generation;

    // (:Version)-[:PART_OF_GROUP]->(:VersionGroup)
    @Relationship(type = "PART_OF_GROUP", direction = Relationship.Direction.INCOMING)
    private List<Version> versions;

    // (:VersionGroup)-[:CONTAINS_REGION]->(:Region)
    @Relationship(type = "CONTAINS_REGION")
    private List<Region> regions;

    // (:VersionGroup)-[:ALLOWS_MOVE_METHOD]->(:MoveMethod)
    @Relationship(type = "ALLOWS_MOVE_METHOD")
    private List<MoveMethod> moveMethods;
}