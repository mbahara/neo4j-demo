package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvPokeathlonStats(
    @CsvBindByName(column = "id") Long id,
    @CsvBindByName(column = "identifier") String identifier
) {}