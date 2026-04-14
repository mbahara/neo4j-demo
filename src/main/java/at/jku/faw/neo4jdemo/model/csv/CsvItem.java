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
public class CsvItem {
    @CsvBindByName(column = "id") private Long id;
    @CsvBindByName(column = "identifier") private String identifier;
    @CsvBindByName(column = "category_id") private Long categoryId;
    @CsvBindByName(column = "cost") private int cost;
    @CsvBindByName(column = "fling_power") private int flingPower;
    @CsvBindByName(column = "fling_effect_id") private Long flingEffectId;
    @CsvBindByName(column = "name") private String name;
    @CsvBindByName(column = "short_effect") private String shortEffect;
    @CsvBindByName(column = "effect") private String effect;
}