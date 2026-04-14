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
public class CsvPokemonSpecies {
    @CsvBindByName(column = "id") private Long id;
    @CsvBindByName(column = "generation_id") private Long generationId;
    @CsvBindByName(column = "evolves_from_species_id") private Long evolvesFromSpeciesId;
    @CsvBindByName(column = "evolution_chain_id") private Long evolutionChainId;
    @CsvBindByName(column = "color_id") private Long colorId;
    @CsvBindByName(column = "shape_id") private Long shapeId;
    @CsvBindByName(column = "habitat_id") private Long habitatId;
    @CsvBindByName(column = "growth_rate_id") private Long growthRateId;
    @CsvBindByName(column = "identifier") private String identifier;
    @CsvBindByName(column = "gender_rate") private int genderRate;
    @CsvBindByName(column = "capture_rate") private int captureRate;
    @CsvBindByName(column = "base_happiness") private int baseHappiness;
    @CsvBindByName(column = "is_baby") private int isBaby;
    @CsvBindByName(column = "is_legendary") private int isLegendary;
    @CsvBindByName(column = "is_mythical") private int isMythical;
    @CsvBindByName(column = "hatch_counter") private int hatchCounter;
    @CsvBindByName(column = "has_gender_differences") private int hasGenderDifferences;
    @CsvBindByName(column = "forms_switchable") private int formsSwitchable;
    @CsvBindByName(column = "order") private int order;
    @CsvBindByName(column = "conquest_order") private int conquestOrder;
    @CsvBindByName(column = "name") private String name;
    @CsvBindByName(column = "genus") private String genus;
}