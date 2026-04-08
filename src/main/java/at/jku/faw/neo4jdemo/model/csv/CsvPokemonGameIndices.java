package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvPokemonGameIndices(
    @CsvBindByName(column = "pokemon_id") int pokemonId,
    @CsvBindByName(column = "version_id") int versionId,
    @CsvBindByName(column = "game_index") int gameIndex
) {}