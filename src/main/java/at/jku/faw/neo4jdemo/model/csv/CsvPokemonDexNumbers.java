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
public class CsvPokemonDexNumbers {
    @CsvBindByName(column = "species_id") private Long speciesId;
    @CsvBindByName(column = "pokedex_id") private Long pokedexId;
    @CsvBindByName(column = "pokedex_number") private int pokedexNumber;
}