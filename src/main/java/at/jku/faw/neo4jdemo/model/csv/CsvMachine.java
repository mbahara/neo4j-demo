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
public class CsvMachine {
    @CsvBindByName(column = "machine_number") private int machineNumber;
    @CsvBindByName(column = "version_group_id") private Long versionGroupId;
    @CsvBindByName(column = "item_id") private Long itemId;
    @CsvBindByName(column = "move_id") private Long moveId;
}