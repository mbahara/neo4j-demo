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
public class CsvContestCombos {
    @CsvBindByName(column = "first_move_id") private Long firstMoveId;
    @CsvBindByName(column = "second_move_id") private Long secondMoveId;
}