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
public class CsvItemFlagMap {
    @CsvBindByName(column = "item_id") private Long itemId;
    @CsvBindByName(column = "item_flag_id") private Long itemFlagId;
}