package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CsvPokemonMoves {
    @CsvBindByName(column = "pokemon_id") private Long pokemonId;
    @CsvBindByName(column = "version_group_id") private Long versionGroupId;
    @CsvBindByName(column = "move_id") private Long moveId;
    @CsvBindByName(column = "pokemon_move_method_id") private Long moveMethodId;
    @CsvBindByName(column = "level") private int level;
    @CsvBindByName(column = "order") private int order;
}