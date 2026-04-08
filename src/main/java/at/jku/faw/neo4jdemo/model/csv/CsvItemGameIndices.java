package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvItemGameIndices(
    @CsvBindByName(column = "item_id") Long itemId,
    @CsvBindByName(column = "generation_id") Long generationId,
    @CsvBindByName(column = "game_index") int gameIndex
) {}