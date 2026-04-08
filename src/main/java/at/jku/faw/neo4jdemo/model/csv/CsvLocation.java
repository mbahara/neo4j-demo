package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvLocation(
		@CsvBindByName(column = "id") Long id,
		@CsvBindByName(column = "region_id") Long regionId,
		@CsvBindByName(column = "identifier") String identifier,
		@CsvBindByName(column = "name") String name,
		@CsvBindByName(column = "subtitle") String subtitle
) {
}
