package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvPokemonStats(
    @CsvBindByName(column = "pokemon_id") Long pokemonId,
    @CsvBindByName(column = "stat_id") Long statId,
    @CsvBindByName(column = "base_stat") int baseStat,
    @CsvBindByName(column = "effort") int effort
) {}