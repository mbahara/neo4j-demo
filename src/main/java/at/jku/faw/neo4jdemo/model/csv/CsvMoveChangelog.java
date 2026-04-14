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
public class CsvMoveChangelog {
    @CsvBindByName(column = "move_id") private Long moveId;
    @CsvBindByName(column = "changed_in_version_group_id") private Long versionGroupId;
    @CsvBindByName(column = "type_id") private Long typeId;
    @CsvBindByName(column = "power") private int power;
    @CsvBindByName(column = "pp") private int pp;
    @CsvBindByName(column = "accuracy") private int accuracy;
    @CsvBindByName(column = "priority") private int priority;
    @CsvBindByName(column = "target_id") private Long targetId;
    @CsvBindByName(column = "effect_id") private Long effectId;
    @CsvBindByName(column = "effect_chance") private Integer effectChance;
}