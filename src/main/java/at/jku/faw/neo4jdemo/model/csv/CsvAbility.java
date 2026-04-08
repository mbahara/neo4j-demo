package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvAbility(
		@CsvBindByName(column = "id") Long id,
		@CsvBindByName(column = "identifier") String identifier,
		@CsvBindByName(column = "is_main_series") int isMainSeries,
		@CsvBindByName(column = "name") String name,
		@CsvBindByName(column = "short_effect") String shortEffect,
		@CsvBindByName(column = "effect") String effect
) {}
