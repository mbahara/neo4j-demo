package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvMoveEffect(
    @CsvBindByName(column = "id") Long id,
    @CsvBindByName(column = "short_effect") String shortEffect,
    @CsvBindByName(column = "effect") String effect
) {}