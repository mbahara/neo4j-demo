package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvPokemonDexNumber(
		@CsvBindByName(column = "species_id") int speciesId,
		@CsvBindByName(column = "pokedex_id") int pokedexId,
		@CsvBindByName(column = "pokedex_number") int pokedexNumber
) {
}
