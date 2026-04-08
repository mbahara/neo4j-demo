package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvAbilityGenerations(
    @CsvBindByName(column = "ability_id") Long abilityId,
    @CsvBindByName(column = "generation_id") Long generationId
) {}