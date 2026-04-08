package at.jku.faw.neo4jdemo.model.neo4j;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Getter
@Setter
@Node("ContestEffect")
public class ContestEffect {
    @Id
    public Long id;

    private int appeal;
    private int jam;
    private String flavorText;
    private String effect;
}