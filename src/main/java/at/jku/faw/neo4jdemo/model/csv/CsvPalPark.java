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
public class CsvPalPark {
    @CsvBindByName(column = "species_id") private Long speciesId;
    @CsvBindByName(column = "area_id") private Long areaId;
    @CsvBindByName(column = "base_score") private int baseScore;
    @CsvBindByName(column = "rate") private int rate;
}