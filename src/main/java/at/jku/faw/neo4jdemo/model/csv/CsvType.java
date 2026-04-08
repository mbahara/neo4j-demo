package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvType(
		@CsvBindByName(column = "id") Long pokemonId,
		@CsvBindByName(column = "identifier") Long typeId
) {}
