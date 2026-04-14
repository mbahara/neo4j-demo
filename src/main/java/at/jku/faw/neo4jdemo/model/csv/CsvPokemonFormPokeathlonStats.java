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
public class CsvPokemonFormPokeathlonStats {
    @CsvBindByName(column = "pokemon_form_id") private Long pokemonFormId;
    @CsvBindByName(column = "pokeathlon_stat_id") private Long pokeathlonStatId;
    @CsvBindByName(column = "minimum_stat") private int minimumStat;
    @CsvBindByName(column = "base_stat") private int baseStat;
    @CsvBindByName(column = "maximum_stat") private int maximumStat;
}