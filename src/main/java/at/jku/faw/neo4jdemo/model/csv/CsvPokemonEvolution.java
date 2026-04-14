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
public class CsvPokemonEvolution {
    @CsvBindByName(column = "id") private Long id;
    @CsvBindByName(column = "evolved_species_id") private Long evolvedSpeciesId;
    @CsvBindByName(column = "evolution_trigger_id") private Long evolutionTriggerId;
    @CsvBindByName(column = "trigger_item_id") private Long triggerItemId;
    @CsvBindByName(column = "minimum_level") private int minimumLevel;
    @CsvBindByName(column = "gender_id") private Long genderId;
    @CsvBindByName(column = "location_id") private Long locationId;
    @CsvBindByName(column = "held_item_id") private Long heldItemId;
    @CsvBindByName(column = "time_of_day") private String timeOfDay;
    @CsvBindByName(column = "known_move_id") private Long knownMoveId;
    @CsvBindByName(column = "known_move_type_id") private Long knownMoveTypeId;
    @CsvBindByName(column = "minimum_happiness") private int minimumHappiness;
    @CsvBindByName(column = "minimum_beauty") private int minimumBeauty;
    @CsvBindByName(column = "minimum_affection") private int minimumAffection;
    @CsvBindByName(column = "relative_physical_stats") private int relativePhysicalStats;
    @CsvBindByName(column = "needs_overworld_rain") private int needsOverworldRain;
    @CsvBindByName(column = "turn_upside_down") private int turnUpsideDown;
}