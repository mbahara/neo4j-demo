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
public class CsvAbility {
	@CsvBindByName(column = "id") private Long id;
	@CsvBindByName(column = "identifier") private String identifier;
	@CsvBindByName(column = "is_main_series") private int isMainSeries;
	@CsvBindByName(column = "name") private String name;
	@CsvBindByName(column = "short_effect") private String shortEffect;
	@CsvBindByName(column = "effect") private String effect;
}
