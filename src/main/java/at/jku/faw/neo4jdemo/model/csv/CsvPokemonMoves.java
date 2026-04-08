package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvPokemonMoves(
		@CsvBindByName(column = "pokemon_id") int pokemonId,
		@CsvBindByName(column = "version_group_id") int versionGroupId,
		@CsvBindByName(column = "move_id") int moveId,
		@CsvBindByName(column = "pokemon_move_method_id") int moveMethodId,
		@CsvBindByName(column = "level") int level,
		@CsvBindByName(column = "order") int order
) {
}
