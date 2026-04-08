package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvPokedexVersionGroups(
    @CsvBindByName(column = "pokedex_id") int pokedexId,
    @CsvBindByName(column = "version_group_id") int versionGroupId
) {}