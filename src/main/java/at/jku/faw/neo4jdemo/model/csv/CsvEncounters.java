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
public class CsvEncounters {
    @CsvBindByName(column = "id") private Long id;
    @CsvBindByName(column = "version_id") private Long versionId;
    @CsvBindByName(column = "location_area_id") private Long locationAreaId;
    @CsvBindByName(column = "pokemon_id") private Long pokemonId;
    @CsvBindByName(column = "min_level") private int minLevel;
    @CsvBindByName(column = "max_level") private int maxLevel;
}