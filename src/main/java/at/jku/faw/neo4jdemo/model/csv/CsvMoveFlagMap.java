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
public class CsvMoveFlagMap {
    @CsvBindByName(column = "move_id") private Long moveId;
    @CsvBindByName(column = "move_flag_id") private Long moveFlagId;
}