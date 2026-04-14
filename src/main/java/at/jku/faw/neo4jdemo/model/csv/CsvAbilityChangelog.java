package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CsvAbilityChangelog {
    @CsvBindByName(column = "id") private Long id;
    @CsvBindByName(column = "ability_id") private Long abilityId;
    @CsvBindByName(column = "changed_in_version_group_id") private Long versionGroupId;
    @CsvBindByName(column = "effect") private String effect;
}