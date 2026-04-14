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
public class CsvSuperContestEffects {
    @CsvBindByName(column = "id") private Long id;
    @CsvBindByName(column = "appeal") private int appeal;
    @CsvBindByName(column = "flavor_text") private String flavorText;
}