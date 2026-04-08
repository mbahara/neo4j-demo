package at.jku.faw.neo4jdemo.model.neo4j;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Getter
@Setter
@Node("PokemonForm")
public class PokemonForm {
    @Id
    public Long id;

    private String identifier;
    private boolean isDefault;
    private boolean isMega;
    private boolean isBattleOnly;

    @Relationship(type = "HAS_FORM", direction = Relationship.Direction.INCOMING)
    private Pokemon pokemon;

    @Relationship(type = "POKEATHLON_PERFORMANCE")
    private List<FormPokeathlonStats> pokeathlonStats;

    // pokemon_form_generations.csv
    @Relationship(type = "APPEARS_IN")
    private List<GameIndex> appearances;
}