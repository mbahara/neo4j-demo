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
public class CsvPokemonAbilities {
    @CsvBindByName(column = "pokemon_id") private Long pokemonId;
    @CsvBindByName(column = "ability_id") private Long abilityId;
    @CsvBindByName(column = "is_hidden") private int isHidden;
    @CsvBindByName(column = "slot") private int slot;
}