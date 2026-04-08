package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvPalParkAreas(
    @CsvBindByName(column = "id") Long id,
    @CsvBindByName(column = "identifier") String identifier
) {}