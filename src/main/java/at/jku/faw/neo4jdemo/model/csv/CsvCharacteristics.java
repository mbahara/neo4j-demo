package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvCharacteristics(
    @CsvBindByName(column = "id") int id,
    @CsvBindByName(column = "stat_id") int statId,
    @CsvBindByName(column = "gene_mod_5") int geneMod5,
    @CsvBindByName(column = "description") String description
) {}