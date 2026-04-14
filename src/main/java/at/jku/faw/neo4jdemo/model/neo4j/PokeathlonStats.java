package at.jku.faw.neo4jdemo.model.neo4j;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Node("PokeathlonStats")
public class PokeathlonStats {
    @Id
    public Long id;

    @Property("identifier")
    private String identifier;
}