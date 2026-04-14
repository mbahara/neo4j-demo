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
public class CsvMoveMeta {
    @CsvBindByName(column = "move_id") private Long moveId;
    @CsvBindByName(column = "meta_category_id") private Long metaCategoryId;
    @CsvBindByName(column = "meta_ailment_id") private Long metaAilmentId;
    @CsvBindByName(column = "min_hits") private int minHits;
    @CsvBindByName(column = "max_hits") private int maxHits;
    @CsvBindByName(column = "min_turns") private int minTurns;
    @CsvBindByName(column = "max_turns") private int maxTurns;
    @CsvBindByName(column = "drain") private int drain;
    @CsvBindByName(column = "healing") private int healing;
    @CsvBindByName(column = "crit_rate") private int critRate;
    @CsvBindByName(column = "ailment_chance") private int ailmentChance;
    @CsvBindByName(column = "flinch_chance") private int flinchChance;
    @CsvBindByName(column = "stat_chance") private int statChance;
}