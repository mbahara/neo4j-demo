package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvLocationGameIndices(
    @CsvBindByName(column = "location_id") Long locationId,
    @CsvBindByName(column = "generation_id") Long generationId,
    @CsvBindByName(column = "game_index") int gameIndex
) {}