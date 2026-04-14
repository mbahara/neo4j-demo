package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvMoveChangelog(
		@CsvBindByName(column = "move_id") Long moveId,
		@CsvBindByName(column = "changed_in_version_group_id") Long versionGroupId,
		@CsvBindByName(column = "type_id") Long typeId,
		@CsvBindByName(column = "power") int power,
		@CsvBindByName(column = "pp") int pp,
		@CsvBindByName(column = "accuracy") int accuracy,
		@CsvBindByName(column = "priority") int priority,
		@CsvBindByName(column = "target_id") Long targetId,
		@CsvBindByName(column = "effect_id") Long effectId,
		@CsvBindByName(column = "effect_chance") Integer effectChance
) {}