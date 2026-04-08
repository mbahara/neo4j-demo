package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvItemFlingEffects(
    @CsvBindByName(column = "id") Long id,
    @CsvBindByName(column = "identifier") String identifier,
    @CsvBindByName(column = "effect") String effect
) {}