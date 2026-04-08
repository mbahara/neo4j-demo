package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvMoveChangelog(
		@CsvBindByName(column = "move_id") int moveId,
		@CsvBindByName(column = "changed_in_version_group_id") int versionGroupId,
		@CsvBindByName(column = "type_id") int typeId,
		@CsvBindByName(column = "power") Integer power,
		@CsvBindByName(column = "pp") Integer pp,
		@CsvBindByName(column = "accuracy") Integer accuracy,
		@CsvBindByName(column = "priority") Integer priority,
		@CsvBindByName(column = "target_id") Integer targetId,
		@CsvBindByName(column = "effect_id") Integer effectId,
		@CsvBindByName(column = "effect_change") Integer effectChange
) {}