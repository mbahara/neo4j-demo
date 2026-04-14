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
public class CsvVersionGroupPokemonMoveMethods {
    @CsvBindByName(column = "version_group_id") private Long versionGroupId;
    @CsvBindByName(column = "pokemon_move_method_id") private Long pokemonMoveMethodId;
}