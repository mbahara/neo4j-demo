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
public class CsvExperience {
    @CsvBindByName(column = "growth_rate_id") private Long growthRateId;
    @CsvBindByName(column = "level") private int level;
    @CsvBindByName(column = "experience") private int experience;
}