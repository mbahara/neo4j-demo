package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvItem(
		@CsvBindByName(column = "id") Long id,
		@CsvBindByName(column = "identifier") String identifier,
		@CsvBindByName(column = "category_id") Long categoryId,
		@CsvBindByName(column = "cost") int cost,
		@CsvBindByName(column = "fling_power") Integer flingPower,
		@CsvBindByName(column = "fling_effect_id") Integer flingEffectId,
		@CsvBindByName(column = "name") String name
) {}