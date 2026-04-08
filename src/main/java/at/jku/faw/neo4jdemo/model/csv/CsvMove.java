package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvMove(
		@CsvBindByName(column = "id") int id,
		@CsvBindByName(column = "identifier") String identifier,
		@CsvBindByName(column = "generation_id") int generationId,
		@CsvBindByName(column = "type_id") int typeId,
		@CsvBindByName(column = "power") Double power,
		@CsvBindByName(column = "pp") Integer pp,
		@CsvBindByName(column = "accuracy") Integer accuracy,
		@CsvBindByName(column = "priority") int priority,
		@CsvBindByName(column = "target_id") int targetId,
		@CsvBindByName(column = "damage_class_id") int damageClassId,
		@CsvBindByName(column = "effect_id") int effectId,
		@CsvBindByName(column = "name") String name
) {}
