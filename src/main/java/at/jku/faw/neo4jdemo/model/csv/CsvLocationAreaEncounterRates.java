package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvLocationAreaEncounterRates(
    @CsvBindByName(column = "location_area_id") Long locationAreaId,
    @CsvBindByName(column = "encounter_method_id") Long encounterMethodId,
    @CsvBindByName(column = "version_id") Long versionId,
    @CsvBindByName(column = "rate") int rate
) {}