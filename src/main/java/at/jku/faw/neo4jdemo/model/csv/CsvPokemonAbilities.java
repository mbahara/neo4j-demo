package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvPokemonAbilities(
    @CsvBindByName(column = "pokemon_id") Long pokemonId,
    @CsvBindByName(column = "ability_id") Long abilityId,
    @CsvBindByName(column = "is_hidden") int isHidden,
    @CsvBindByName(column = "slot") int slot
) {}