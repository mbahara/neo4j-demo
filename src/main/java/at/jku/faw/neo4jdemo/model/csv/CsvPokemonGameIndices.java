package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvPokemonGameIndices(
    @CsvBindByName(column = "pokemon_id") Long pokemonId,
    @CsvBindByName(column = "version_id") Long versionId,
    @CsvBindByName(column = "game_index") int gameIndex
) {}