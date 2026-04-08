package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvPokemonForm(
		@CsvBindByName(column = "id") Long id,
		@CsvBindByName(column = "identifier") String identifier,
		@CsvBindByName(column = "pokemon_id") Long pokemonId,
		@CsvBindByName(column = "is_default") int isDefault,
		@CsvBindByName(column = "is_battle_only") int isBattleOnly,
		@CsvBindByName(column = "is_mega") int isMega
) {}
