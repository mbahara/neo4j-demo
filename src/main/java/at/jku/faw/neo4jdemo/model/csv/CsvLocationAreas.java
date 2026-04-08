package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvLocationAreas(
    @CsvBindByName(column = "id") int id,
    @CsvBindByName(column = "location_id") int locationId,
    @CsvBindByName(column = "game_index") int gameIndex,
    @CsvBindByName(column = "identifier") String identifier,
    @CsvBindByName(column = "name") String name
) {}