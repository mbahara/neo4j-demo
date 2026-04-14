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
public class CsvPokemon {
    @CsvBindByName(column = "id") private Long id;
    @CsvBindByName(column = "identifier") private String identifier;
    @CsvBindByName(column = "species_id") private Long speciesId;
    @CsvBindByName(column = "height") private int height;
    @CsvBindByName(column = "weight") private int weight;
    @CsvBindByName(column = "base_experience") private int baseExperience;
    @CsvBindByName(column = "order") private int order;
    @CsvBindByName(column = "is_default") private int isDefault;
}