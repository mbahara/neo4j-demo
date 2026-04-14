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
public class CsvTypeGameIndices {
    @CsvBindByName(column = "type_id") private Long typeId;
    @CsvBindByName(column = "generation_id") private Long generationId;
    @CsvBindByName(column = "game_index") private int gameIndex;
}