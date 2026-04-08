package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvItemCategories(
    @CsvBindByName(column = "id") int id,
    @CsvBindByName(column = "pocket_id") int pocketId,
    @CsvBindByName(column = "identifier") String identifier,
    @CsvBindByName(column = "name") String name
) {}