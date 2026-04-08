package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvSuperContestCombos(
    @CsvBindByName(column = "first_move_id") Long firstMoveId,
    @CsvBindByName(column = "second_move_id") Long secondMoveId
) {}