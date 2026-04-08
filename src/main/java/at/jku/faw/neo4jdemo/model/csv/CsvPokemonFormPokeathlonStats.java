package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvPokemonFormPokeathlonStats(
    @CsvBindByName(column = "pokemon_form_id") Long pokemonFormId,
    @CsvBindByName(column = "pokeathlon_stat_id") Long pokeathlonStatId,
    @CsvBindByName(column = "minimum_stat") int minimumStat,
    @CsvBindByName(column = "base_stat") int baseStat,
    @CsvBindByName(column = "maximum_stat") int maximumStat
) {}