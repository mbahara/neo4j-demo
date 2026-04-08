package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvItemGameIndices(
    @CsvBindByName(column = "item_id") int itemId,
    @CsvBindByName(column = "generation_id") int generationId,
    @CsvBindByName(column = "game_index") int gameIndex
) {}