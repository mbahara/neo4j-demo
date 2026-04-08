package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvVersions(
    @CsvBindByName(column = "id") Long id,
    @CsvBindByName(column = "version_group_id") Long versionGroupId,
    @CsvBindByName(column = "identifier") String identifier,
    @CsvBindByName(column = "name") String name
) {}