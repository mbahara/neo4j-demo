package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvPokemonType(
		@CsvBindByName(column = "pokemon_id") int pokemonId,
		@CsvBindByName(column = "type_id") int typeId,
		@CsvBindByName(column = "slot") int slot
) {
}
