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
public class CsvVersionGroupRegions {
    @CsvBindByName(column = "version_group_id") private Long versionGroupId;
    @CsvBindByName(column = "region_id") private Long regionId;
}