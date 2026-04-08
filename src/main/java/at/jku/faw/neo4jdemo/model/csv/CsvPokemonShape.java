package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvPokemonShape(
    @CsvBindByName(column = "pokemon_shape_id") Long pokemonShapeId,
    @CsvBindByName(column = "name") String name,
    @CsvBindByName(column = "awesome_name") int awesomeName,
    @CsvBindByName(column = "description") String description
) {}