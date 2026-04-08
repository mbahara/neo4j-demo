package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvStats(
    @CsvBindByName(column = "id") Long id,
    @CsvBindByName(column = "damage_class_id") Long damageClassId,
    @CsvBindByName(column = "identifier") String identifier,
    @CsvBindByName(column = "is_battle_only") int isBattleOnly,
    @CsvBindByName(column = "game_index") int gameIndex
) {}