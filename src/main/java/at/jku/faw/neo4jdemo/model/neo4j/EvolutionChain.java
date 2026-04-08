package at.jku.faw.neo4jdemo.model.neo4j;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Getter
@Setter
@Node("EvolutionChain")
public class EvolutionChain {
    @Id
    public Long id;

    // Some babies only appear if the parent holds this item
    @Relationship(type = "BABY_TRIGGER_ITEM")
    private Item babyTriggerItem;

    @Relationship(type = "MEMBERS", direction = Relationship.Direction.INCOMING)
    private List<PokemonSpecies> species;
}