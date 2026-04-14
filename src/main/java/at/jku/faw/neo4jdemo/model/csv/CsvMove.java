package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvMove(
		@CsvBindByName(column = "id") Long id,
		@CsvBindByName(column = "identifier") String identifier,
		@CsvBindByName(column = "generation_id") Long generationId,
		@CsvBindByName(column = "type_id") Long typeId,
		@CsvBindByName(column = "power") int power,
		@CsvBindByName(column = "pp") int pp,
		@CsvBindByName(column = "accuracy") int accuracy,
		@CsvBindByName(column = "priority") int priority,
		@CsvBindByName(column = "target_id") Long targetId,
		@CsvBindByName(column = "damage_class_id") Long damageClassId,
		@CsvBindByName(column = "effect_id") Long effectId,
		@CsvBindByName(column = "effect_chance") int effectChance,
		@CsvBindByName(column = "contest_type_id") Long contestTypeId,
		@CsvBindByName(column = "contest_effect_id") Long contestEffectId,
		@CsvBindByName(column = "super_contest_effect_id") Long superContestEffectId,
		@CsvBindByName(column = "name") String name
) {}
