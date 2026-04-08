package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvMoveFlagMap(
    @CsvBindByName(column = "move_id") Long moveId,
    @CsvBindByName(column = "move_flag_id") Long moveFlagId
) {}