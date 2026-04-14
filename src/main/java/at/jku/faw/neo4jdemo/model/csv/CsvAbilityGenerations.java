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
public class CsvAbilityGenerations {
    @CsvBindByName(column = "ability_id") private Long abilityId;
    @CsvBindByName(column = "generation_id") private Long generationId;
}