package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvVersions(
    @CsvBindByName(column = "id") int id,
    @CsvBindByName(column = "version_group_id") int versionGroupId,
    @CsvBindByName(column = "identifier") String identifier,
    @CsvBindByName(column = "name") String name
) {}