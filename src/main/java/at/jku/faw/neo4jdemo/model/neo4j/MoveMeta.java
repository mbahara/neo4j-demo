package at.jku.faw.neo4jdemo.model.neo4j;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Node("MoveMeta")
public class MoveMeta {
    @Id @GeneratedValue
    public Long id;

    private int minHits;
    private int maxHits;
    private int minTurns;
    private int maxTurns;
    private int drain;
    private int healing;
    private int critRate;
    private int ailmentChance;
    private int flinchChance;
    private int statChance;
    private Long moveId;

    @Relationship(type = "CAUSES_AILMENT")
    private MoveAilment ailment;

    @Relationship(type = "IN_META_CATEGORY")
    private MoveCategory category;
}