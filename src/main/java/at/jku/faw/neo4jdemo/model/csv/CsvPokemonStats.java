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
public class CsvPokemonStats {
    @CsvBindByName(column = "pokemon_id") private Long pokemonId;
    @CsvBindByName(column = "stat_id") private Long statId;
    @CsvBindByName(column = "base_stat") private int baseStat;
    @CsvBindByName(column = "effort") private int effort;
}