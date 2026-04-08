package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvPokemonItems(
    @CsvBindByName(column = "pokemon_id") int pokemonId,
    @CsvBindByName(column = "version_id") int versionId,
    @CsvBindByName(column = "item_id") int itemId,
    @CsvBindByName(column = "rarity") int rarity
) {}