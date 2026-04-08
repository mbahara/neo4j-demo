package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvPokemonEggGroups(
    @CsvBindByName(column = "species_id") int speciesId,
    @CsvBindByName(column = "egg_group_id") int eggGroupId
) {}