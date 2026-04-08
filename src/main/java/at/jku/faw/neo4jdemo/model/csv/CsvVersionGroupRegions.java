package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvVersionGroupRegions(
    @CsvBindByName(column = "version_group_id") Long versionGroupId,
    @CsvBindByName(column = "region_id") Long regionId
) {}