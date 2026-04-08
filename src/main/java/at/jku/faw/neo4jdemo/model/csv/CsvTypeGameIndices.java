package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvTypeGameIndices(
    @CsvBindByName(column = "type_id") Long typeId,
    @CsvBindByName(column = "generation_id") Long generationId,
    @CsvBindByName(column = "game_index") int gameIndex
) {}