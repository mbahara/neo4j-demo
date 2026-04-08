package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvPokemonAbilities(
    @CsvBindByName(column = "pokemon_id") int pokemonId,
    @CsvBindByName(column = "ability_id") int abilityId,
    @CsvBindByName(column = "is_hidden") int isHidden,
    @CsvBindByName(column = "slot") int slot
) {}