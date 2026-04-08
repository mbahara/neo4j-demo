package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvNature (
		@CsvBindByName(column = "id") Long id,
		@CsvBindByName(column = "identifier") String identifier,
		@CsvBindByName(column = "decreased_stat_id") Long decreasedStatId,
		@CsvBindByName(column = "increased_stat_id") Long increasedStatId)
{}
