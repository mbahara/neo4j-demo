package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvTypeGameIndices(
    @CsvBindByName(column = "type_id") int typeId,
    @CsvBindByName(column = "generation_id") int generationId,
    @CsvBindByName(column = "game_index") int gameIndex
) {}