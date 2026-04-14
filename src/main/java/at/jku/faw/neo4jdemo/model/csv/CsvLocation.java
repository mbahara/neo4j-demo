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
public class CsvLocation {
    @CsvBindByName(column = "id") private Long id;
    @CsvBindByName(column = "region_id") private Long regionId;
    @CsvBindByName(column = "identifier") private String identifier;
    @CsvBindByName(column = "name") private String name;
    @CsvBindByName(column = "subtitle") private String subtitle;
}