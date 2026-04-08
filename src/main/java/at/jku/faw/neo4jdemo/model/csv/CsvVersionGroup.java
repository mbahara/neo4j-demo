package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvVersionGroup(
		@CsvBindByName(column = "id") int id,
		@CsvBindByName(column = "identifier") String identifier,
		@CsvBindByName(column = "generation_id") int generationId,
		@CsvBindByName(column = "order") int order
) {
}
