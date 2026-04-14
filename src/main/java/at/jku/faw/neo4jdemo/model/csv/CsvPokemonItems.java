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
public class CsvPokemonItems {
    @CsvBindByName(column = "pokemon_id") private Long pokemonId;
    @CsvBindByName(column = "version_id") private Long versionId;
    @CsvBindByName(column = "item_id") private Long itemId;
    @CsvBindByName(column = "rarity") private int rarity;
}