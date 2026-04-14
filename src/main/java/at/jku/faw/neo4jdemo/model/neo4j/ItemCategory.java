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
@Node("ItemCategory")
public class ItemCategory {
    @Id
    public Long id;

    private String identifier;
    private String name;

    // (:ItemCategory)-[:IN_POCKET]->(:ItemPocket)
    @Relationship(type = "IN_POCKET")
    private ItemPocket pocket;
}