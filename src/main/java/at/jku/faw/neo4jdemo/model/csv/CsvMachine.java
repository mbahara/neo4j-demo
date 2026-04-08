package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvMachine(
		@CsvBindByName(column = "machine_number") int machineNumber,
		@CsvBindByName(column = "version_group_id") int versionGroupId,
		@CsvBindByName(column = "item_id") int itemId,
		@CsvBindByName(column = "move_id") int moveId
) {}