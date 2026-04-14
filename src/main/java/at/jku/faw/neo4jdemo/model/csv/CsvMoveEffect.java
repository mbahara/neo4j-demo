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
public class CsvMoveEffect {
    @CsvBindByName(column = "id") private Long id;
    @CsvBindByName(column = "short_effect") private String shortEffect;
    @CsvBindByName(column = "effect") private String effect;
}