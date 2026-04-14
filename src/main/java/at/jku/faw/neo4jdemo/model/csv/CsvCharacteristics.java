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
public class CsvCharacteristics {
    @CsvBindByName(column = "id") private Long id;
    @CsvBindByName(column = "stat_id") private Long statId;
    @CsvBindByName(column = "gene_mod_5") private int geneMod5;
    @CsvBindByName(column = "description") private String description;
}