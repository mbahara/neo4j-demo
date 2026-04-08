package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvPokedexes(
    @CsvBindByName(column = "id") Long id,
    @CsvBindByName(column = "region_id") Long regionId,
    @CsvBindByName(column = "identifier") String identifier,
    @CsvBindByName(column = "is_main_series") int isMainSeries
) {}