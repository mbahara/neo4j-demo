package at.jku.faw.neo4jdemo.model.neo4j;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Getter
@Setter
@Node("Stat")
public class Stats {
    @Id
    public Long id;

    private String identifier;
    private boolean isBattleOnly;
    private Integer gameIndex;

    // (:Stat)-[:HAS_DAMAGE_CLASS]->(:DamageClass)
    @Relationship(type = "HAS_DAMAGE_CLASS")
    private DamageClass damageClass;
}