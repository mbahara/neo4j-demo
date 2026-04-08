package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvPalPark(
    @CsvBindByName(column = "species_id") int speciesId,
    @CsvBindByName(column = "area_id") int areaId,
    @CsvBindByName(column = "base_score") int baseScore,
    @CsvBindByName(column = "rate") int rate
) {}