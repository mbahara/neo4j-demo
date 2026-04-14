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
public class CsvPokemonForm {
    @CsvBindByName(column = "id") private Long id;
    @CsvBindByName(column = "identifier") private String identifier;
    @CsvBindByName(column = "pokemon_id") private Long pokemonId;
    @CsvBindByName(column = "is_default") private int isDefault;
    @CsvBindByName(column = "is_battle_only") private int isBattleOnly;
    @CsvBindByName(column = "is_mega") private int isMega;
}