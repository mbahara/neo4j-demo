package at.jku.faw.neo4jdemo.model.neo4j;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Getter
@Setter
@Node("PokemonStat")
public class PokemonStats {
    @Id
    public Long id;
    private Long pokemonId;
    private Long statId;
    private int baseStat;
    private int effort;
}