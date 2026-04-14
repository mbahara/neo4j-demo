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
public class CsvMove {
    @CsvBindByName(column = "id") private Long id;
    @CsvBindByName(column = "identifier") private String identifier;
    @CsvBindByName(column = "generation_id") private Long generationId;
    @CsvBindByName(column = "type_id") private Long typeId;
    @CsvBindByName(column = "power") private int power;
    @CsvBindByName(column = "pp") private int pp;
    @CsvBindByName(column = "accuracy") private int accuracy;
    @CsvBindByName(column = "priority") private int priority;
    @CsvBindByName(column = "target_id") private Long targetId;
    @CsvBindByName(column = "damage_class_id") private Long damageClassId;
    @CsvBindByName(column = "effect_id") private Long effectId;
    @CsvBindByName(column = "effect_chance") private int effectChance;
    @CsvBindByName(column = "contest_type_id") private Long contestTypeId;
    @CsvBindByName(column = "contest_effect_id") private Long contestEffectId;
    @CsvBindByName(column = "super_contest_effect_id") private Long superContestEffectId;
    @CsvBindByName(column = "name") private String name;
}