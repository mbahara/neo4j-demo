package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvEncounters(
		@CsvBindByName(column = "id") Long id,
		@CsvBindByName(column = "version_id") Long versionId,
		@CsvBindByName(column = "location_area_id") Long locationAreaId,
		@CsvBindByName(column = "pokemon_id") Long pokemonId,
		@CsvBindByName(column = "min_level") int minLevel,
		@CsvBindByName(column = "max_level") int maxLevel
) {
}
