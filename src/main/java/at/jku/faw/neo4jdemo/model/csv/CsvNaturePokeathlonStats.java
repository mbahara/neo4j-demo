package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvNaturePokeathlonStats(
    @CsvBindByName(column = "nature_id") Long natureId,
    @CsvBindByName(column = "pokeathlon_stat_id") Long pokeathlonStatId,
    @CsvBindByName(column = "max_change") int maxChange
) {}