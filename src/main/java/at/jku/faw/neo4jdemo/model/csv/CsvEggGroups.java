package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvEggGroups(
    @CsvBindByName(column = "id") int id,
    @CsvBindByName(column = "identifier") String identifier,
    @CsvBindByName(column = "name") String name
) {}