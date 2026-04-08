package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvPokemonMoves(
		@CsvBindByName(column = "pokemon_id") Long pokemonId,
		@CsvBindByName(column = "version_group_id") Long versionGroupId,
		@CsvBindByName(column = "move_id") Long moveId,
		@CsvBindByName(column = "pokemon_move_method_id") Long moveMethodId,
		@CsvBindByName(column = "level") int level,
		@CsvBindByName(column = "order") int order
) {
}
