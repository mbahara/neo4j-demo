package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvPokemonFormGenerations(
    @CsvBindByName(column = "pokemon_form_id") Long pokemonFormId,
    @CsvBindByName(column = "generation_id") Long generationId,
    @CsvBindByName(column = "game_index") int gameIndex
) {}