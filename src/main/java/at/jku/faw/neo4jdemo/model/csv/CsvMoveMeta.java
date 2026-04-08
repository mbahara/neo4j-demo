package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvMoveMeta(
    @CsvBindByName(column = "move_id") int moveId,
    @CsvBindByName(column = "meta_category_id") int metaCategoryId,
    @CsvBindByName(column = "meta_ailment_id") int metaAilmentId,
    @CsvBindByName(column = "min_hits") int minHits,
    @CsvBindByName(column = "max_hits") int maxHits,
    @CsvBindByName(column = "min_turns") int minTurns,
    @CsvBindByName(column = "max_turns") int maxTurns,
    @CsvBindByName(column = "drain") int drain,
    @CsvBindByName(column = "healing") int healing,
    @CsvBindByName(column = "crit_rate") int critRate,
    @CsvBindByName(column = "ailment_chance") int ailmentChance,
    @CsvBindByName(column = "flinch_chance") int flinchChance,
    @CsvBindByName(column = "stat_chance") int statChance
) {}