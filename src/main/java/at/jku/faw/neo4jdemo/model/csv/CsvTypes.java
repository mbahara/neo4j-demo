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
public class CsvTypes {
    @CsvBindByName(column = "id") private Long id;
    @CsvBindByName(column = "identifier") private String identifier;
    @CsvBindByName(column = "generation_id") private Long generationId;
    @CsvBindByName(column = "damage_class_id") private Long damageClassId;
}