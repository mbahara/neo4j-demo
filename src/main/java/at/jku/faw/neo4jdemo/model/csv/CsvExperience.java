package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvExperience(
    @CsvBindByName(column = "growth_rate_id") int growthRateId,
    @CsvBindByName(column = "level") int level,
    @CsvBindByName(column = "experience") long experience
) {}