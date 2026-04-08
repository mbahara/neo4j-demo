package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvNatureBattleStylePreferences(
    @CsvBindByName(column = "nature_id") int natureId,
    @CsvBindByName(column = "move_battle_style_id") int moveBattleStyleId,
    @CsvBindByName(column = "low_hp_preference") int lowHpPreference,
    @CsvBindByName(column = "high_hp_preference") int highHpPreference
) {}