package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvAbilityChangelog(
		@CsvBindByName(column = "id") Long id,
		@CsvBindByName(column = "ability_id") Long abilityId,
		@CsvBindByName(column = "changed_in_version_group_id") Long versionGroupId,
		@CsvBindByName(column = "effect") String effect
) {
}
