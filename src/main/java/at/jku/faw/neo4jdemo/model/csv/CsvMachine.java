package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvMachine(
		@CsvBindByName(column = "machine_number") int machineNumber,
		@CsvBindByName(column = "version_group_id") Long versionGroupId,
		@CsvBindByName(column = "item_id") Long itemId,
		@CsvBindByName(column = "move_id") Long moveId
) {}