package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvPokemonDexNumbers(
    @CsvBindByName(column = "species_id") Long speciesId,
    @CsvBindByName(column = "pokedex_id") Long pokedexId,
    @CsvBindByName(column = "pokedex_number") int pokedexNumber
) {}