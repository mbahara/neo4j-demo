package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvContestEffects(
    @CsvBindByName(column = "id") int id,
    @CsvBindByName(column = "appeal") int appeal,
    @CsvBindByName(column = "jam") int jam,
    @CsvBindByName(column = "flavor_text") String flavorText,
    @CsvBindByName(column = "effect") String effect
) {}