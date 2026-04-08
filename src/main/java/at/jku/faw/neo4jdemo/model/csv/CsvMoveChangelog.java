package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvMoveChangelog(
		@CsvBindByName(column = "move_id") Long moveId,
		@CsvBindByName(column = "changed_in_version_group_id") Long versionGroupId,
		@CsvBindByName(column = "type_id") Long typeId,
		@CsvBindByName(column = "power") Integer power,
		@CsvBindByName(column = "pp") Integer pp,
		@CsvBindByName(column = "accuracy") Integer accuracy,
		@CsvBindByName(column = "priority") Integer priority,
		@CsvBindByName(column = "target_id") Integer targetId,
		@CsvBindByName(column = "effect_id") Integer effectId,
		@CsvBindByName(column = "effect_change") Integer effectChange
) {}