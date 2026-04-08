package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvPokemonItems(
    @CsvBindByName(column = "pokemon_id") Long pokemonId,
    @CsvBindByName(column = "version_id") Long versionId,
    @CsvBindByName(column = "item_id") Long itemId,
    @CsvBindByName(column = "rarity") int rarity
) {}