package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvPokemonSpecies(
		@CsvBindByName(column = "id") int id,
		@CsvBindByName(column = "identifier") String identifier,
		@CsvBindByName(column = "generation_id") Integer generationId,
		@CsvBindByName(column = "evolves_from_species_id") Integer evolvesFromSpeciesId,
		@CsvBindByName(column = "evolution_chain_id") Integer evolutionChainId,
		@CsvBindByName(column = "color_id") Integer colorId,
		@CsvBindByName(column = "shape_id") Integer shapeId,
		@CsvBindByName(column = "habitat_id") Integer habitatId,
		@CsvBindByName(column = "gender_rate") int genderRate,
		@CsvBindByName(column = "capture_rate") int captureRate,
		@CsvBindByName(column = "is_baby") int isBaby, // 0 or 1
		@CsvBindByName(column = "growth_rate_id") int growthRateId,
		@CsvBindByName(column = "is_legendary") int isLegendary,
		@CsvBindByName(column = "is_mythical") int isMythical,
		@CsvBindByName(column = "order") int order,
		@CsvBindByName(column = "name") String name,
		@CsvBindByName(column = "genus") String genus
) {}
