package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvEvolutionChains(
    @CsvBindByName(column = "id") int id,
    @CsvBindByName(column = "baby_trigger_item_id") int babyTriggerItemId
) {}