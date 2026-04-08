package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvPokemonStats(
    @CsvBindByName(column = "pokemon_id") int pokemonId,
    @CsvBindByName(column = "stat_id") int statId,
    @CsvBindByName(column = "base_stat") int baseStat,
    @CsvBindByName(column = "effort") int effort
) {}