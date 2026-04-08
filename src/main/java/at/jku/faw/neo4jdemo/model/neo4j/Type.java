package at.jku.faw.neo4jdemo.model.neo4j;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Getter
@Setter
@Node("Type")
public class Type {
    @Id
    public Long id;

    private String identifier;

    // (:Type)-[:INTRODUCED_IN]->(:Generation)
    @Relationship(type = "INTRODUCED_IN")
    private Generation generation;

    // Self-referential relationship for Type Matchups
    @Relationship(type = "EFFICACY_AGAINST")
    private List<TypeEfficacy> efficacyAgainst;

    // Game indices per generation
    @Relationship(type = "HAS_INDEX")
    private List<GameIndex> gameIndices;

    // (:Type)-[:DEFAULT_DAMAGE_CLASS]->(:DamageClass)
    @Relationship(type = "DEFAULT_DAMAGE_CLASS")
    private DamageClass defaultDamageClass;
}