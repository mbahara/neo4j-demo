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
public class CsvNaturePokeathlonStats {
    @CsvBindByName(column = "nature_id") private Long natureId;
    @CsvBindByName(column = "pokeathlon_stat_id") private Long pokeathlonStatId;
    @CsvBindByName(column = "max_change") private int maxChange;
}