package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvPokemonSpecies(
		@CsvBindByName(column = "id") Long id,
		@CsvBindByName(column = "generation_id") Long generationId,
		@CsvBindByName(column = "evolves_from_species_id") Long evolvesFromSpeciesId,
		@CsvBindByName(column = "evolution_chain_id") Long evolutionChainId,
		@CsvBindByName(column = "color_id") Long colorId,
		@CsvBindByName(column = "shape_id") Long shapeId,
		@CsvBindByName(column = "habitat_id") Long habitatId,
		@CsvBindByName(column = "growth_rate_id") Long growthRateId,
		@CsvBindByName(column = "identifier") String identifier,
		@CsvBindByName(column = "gender_rate") int genderRate,
		@CsvBindByName(column = "capture_rate") int captureRate,
		@CsvBindByName(column = "is_baby") int isBaby, // 0 or 1
		@CsvBindByName(column = "is_legendary") int isLegendary,
		@CsvBindByName(column = "is_mythical") int isMythical,
		@CsvBindByName(column = "order") int order,
		@CsvBindByName(column = "name") String name,
		@CsvBindByName(column = "genus") String genus
) {}
