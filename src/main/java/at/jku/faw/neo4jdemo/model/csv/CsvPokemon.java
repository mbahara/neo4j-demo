package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvPokemon(
		@CsvBindByName(column = "id") int id,
		@CsvBindByName(column = "identifier") String identifier,
		@CsvBindByName(column = "species_id") int speciesId,
		@CsvBindByName(column = "height") int height,
		@CsvBindByName(column = "weight") int weight,
		@CsvBindByName(column = "base_experience") int baseExperience,
		@CsvBindByName(column = "order") int order,
		@CsvBindByName(column = "is_default") int isDefault
) {}
