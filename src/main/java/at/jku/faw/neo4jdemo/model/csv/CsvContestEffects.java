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
public class CsvContestEffects {
    @CsvBindByName(column = "id") private Long id;
    @CsvBindByName(column = "appeal") private int appeal;
    @CsvBindByName(column = "jam") private int jam;
    @CsvBindByName(column = "flavor_text") private String flavorText;
    @CsvBindByName(column = "effect") private String effect;
}