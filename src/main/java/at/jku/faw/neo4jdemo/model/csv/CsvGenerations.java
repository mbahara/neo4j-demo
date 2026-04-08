package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvGenerations(
    @CsvBindByName(column = "id") Long id,
	@CsvBindByName(column = "identifier") String identifier,
	@CsvBindByName(column = "name") String name,
	@CsvBindByName(column = "main_region_id") Long mainRegionId
) {}