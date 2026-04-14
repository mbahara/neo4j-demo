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
public class CsvStats {
    @CsvBindByName(column = "id") private Long id;
    @CsvBindByName(column = "damage_class_id") private Long damageClassId;
    @CsvBindByName(column = "identifier") private String identifier;
    @CsvBindByName(column = "is_battle_only") private int isBattleOnly;
    @CsvBindByName(column = "game_index") private int gameIndex;
}