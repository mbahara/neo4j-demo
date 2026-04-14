package at.jku.faw.neo4jdemo.model.neo4j;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RelationshipProperties
public class AreaEncounterRate {
    @Id
    @GeneratedValue
    public String id;

    @Property("rate")
    public int rate;
    @Property("versionId")
    public Long versionId;

    @TargetNode
    private EncounterMethod encounterMethod;
}