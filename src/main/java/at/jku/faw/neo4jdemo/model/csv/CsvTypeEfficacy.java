package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvTypeEfficacy(
    @CsvBindByName(column = "damage_type_id") int damageTypeId,
    @CsvBindByName(column = "target_type_id") int targetTypeId,
    @CsvBindByName(column = "damage_factor") int damageFactor
) {}