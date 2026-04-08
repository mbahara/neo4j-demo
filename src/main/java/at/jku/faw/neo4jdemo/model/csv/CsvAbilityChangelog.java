package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvAbilityChangelog(
		@CsvBindByName(column = "id") int id,
		@CsvBindByName(column = "ability_id") int abilityId,
		@CsvBindByName(column = "changed_in_version_group_id") int versionGroupId,
		@CsvBindByName(column = "effect") String effect
) {
}
