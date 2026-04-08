package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvSuperContestEffects(
    @CsvBindByName(column = "id") Long id,
    @CsvBindByName(column = "appeal") int appeal,
    @CsvBindByName(column = "flavor_text") String flavorText
) {}