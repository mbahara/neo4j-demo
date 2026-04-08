package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvLocationGameIndices(
    @CsvBindByName(column = "location_id") int locationId,
    @CsvBindByName(column = "generation_id") int generationId,
    @CsvBindByName(column = "game_index") int gameIndex
) {}