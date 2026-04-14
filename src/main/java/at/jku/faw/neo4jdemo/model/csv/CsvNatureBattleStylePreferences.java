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
public class CsvNatureBattleStylePreferences {
    @CsvBindByName(column = "nature_id") private Long natureId;
    @CsvBindByName(column = "move_battle_style_id") private Long moveBattleStyleId;
    @CsvBindByName(column = "low_hp_preference") private int lowHpPreference;
    @CsvBindByName(column = "high_hp_preference") private int highHpPreference;
}