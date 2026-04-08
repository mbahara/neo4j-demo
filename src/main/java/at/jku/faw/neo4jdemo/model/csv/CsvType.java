package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvType(
		@CsvBindByName(column = "id") int pokemonId,
		@CsvBindByName(column = "identifier") int typeId
) {}
