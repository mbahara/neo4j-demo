package at.jku.faw.neo4jdemo.model.neo4j;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Getter
@Setter
@Node("Area")
public class Area {
    @Id
    public Long id;

    private String identifier;
    private String name;

    // (:Area)-[:PART_OF]->(:Location)
    @Relationship(type = "PART_OF")
    private Location location;

    @Relationship(type = "HAS_ENCOUNTER_RATE")
    private List<AreaEncounterRate> encounterRates;
}