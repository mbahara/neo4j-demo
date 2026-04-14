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
public class CsvPokemonShape {
    @CsvBindByName(column = "pokemon_shape_id") private Long pokemonShapeId;
    @CsvBindByName(column = "name") private String name;
    @CsvBindByName(column = "awesome_name") private int awesomeName;
    @CsvBindByName(column = "description") private String description;
}