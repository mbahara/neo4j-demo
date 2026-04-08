package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvItemFlagMap(
    @CsvBindByName(column = "item_id") int itemId,
    @CsvBindByName(column = "item_flag_id") int itemFlagId
) {}