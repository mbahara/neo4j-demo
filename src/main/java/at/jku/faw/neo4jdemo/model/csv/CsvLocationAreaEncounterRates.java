package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvLocationAreaEncounterRates(
    @CsvBindByName(column = "location_area_id") int locationAreaId,
    @CsvBindByName(column = "encounter_method_id") int encounterMethodId,
    @CsvBindByName(column = "version_id") int versionId,
    @CsvBindByName(column = "rate") int rate
) {}