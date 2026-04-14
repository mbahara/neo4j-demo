package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvItem(
		@CsvBindByName(column = "id") Long id,
		@CsvBindByName(column = "identifier") String identifier,
		@CsvBindByName(column = "category_id") Long categoryId,
		@CsvBindByName(column = "cost") int cost,
		@CsvBindByName(column = "fling_power") int flingPower,
		@CsvBindByName(column = "fling_effect_id") Long flingEffectId,
		@CsvBindByName(column = "name") String name,
		@CsvBindByName(column = "short_effect") String shortEffect,
		@CsvBindByName(column = "effect") String effect
) {}