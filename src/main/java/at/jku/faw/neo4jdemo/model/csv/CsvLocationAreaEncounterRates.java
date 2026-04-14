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
public class CsvLocationAreaEncounterRates {
    @CsvBindByName(column = "location_area_id") private Long locationAreaId;
    @CsvBindByName(column = "encounter_method_id") private Long encounterMethodId;
    @CsvBindByName(column = "version_id") private Long versionId;
    @CsvBindByName(column = "rate") private int rate;
}