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
public class CsvTypeEfficacy {
    @CsvBindByName(column = "damage_type_id") private Long damageTypeId;
    @CsvBindByName(column = "target_type_id") private Long targetTypeId;
    @CsvBindByName(column = "damage_factor") private int damageFactor;
}