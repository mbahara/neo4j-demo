package at.jku.faw.neo4jdemo.model.csv;

import com.opencsv.bean.CsvBindByName;

public record CsvPokemonEvolution(
    @CsvBindByName(column = "id") int id,
    @CsvBindByName(column = "evolved_species_id") int evolvedSpeciesId,
    @CsvBindByName(column = "evolution_trigger_id") int evolutionTriggerId,
    @CsvBindByName(column = "trigger_item_id") int triggerItemId,
    @CsvBindByName(column = "minimum_level") int minimumLevel,
    @CsvBindByName(column = "gender_id") int genderId,
    @CsvBindByName(column = "location_id") int locationId,
    @CsvBindByName(column = "held_item_id") int heldItemId,
    @CsvBindByName(column = "time_of_day") int timeOfDay,
    @CsvBindByName(column = "known_move_id") int knownMoveId,
    @CsvBindByName(column = "known_move_type_id") int knownMoveTypeId,
    @CsvBindByName(column = "minimum_happiness") int minimumHappiness,
    @CsvBindByName(column = "minimum_beauty") int minimumBeauty,
    @CsvBindByName(column = "minimum_affection") int minimumAffection,
    @CsvBindByName(column = "relative_physical_stats") int relativePhysicalStats,
    @CsvBindByName(column = "needs_overworld_rain") int needsOverworldRain,
    @CsvBindByName(column = "turn_upside_down") int turnUpsideDown
) {}