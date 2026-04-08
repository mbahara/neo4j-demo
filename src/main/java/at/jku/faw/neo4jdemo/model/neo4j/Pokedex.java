package at.jku.faw.neo4jdemo.model.neo4j;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Getter
@Setter
@Node("Pokedex")
public class Pokedex {
    @Id
    public Long id;

    private String identifier;
    private boolean isMainSeries;

    @Relationship(type = "REGION_SCOPE")
    private Region region;

    // (:Pokedex)-[:USED_IN]->(:VersionGroup)
    @Relationship(type = "USED_IN")
    private List<VersionGroup> versionGroups;
}