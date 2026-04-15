package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvPokemonDexNumbers;
import at.jku.faw.neo4jdemo.model.csv.CsvPokemonEvolution;
import at.jku.faw.neo4jdemo.repository.csv.CsvEvolutionChainsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvPalParkRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonDexNumbersRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonEggGroupsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonEvolutionRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonSpeciesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.LegendaryRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.PalParkEncounterRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.PokedexEntryRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.PokemonSpeciesRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PokemonSpeciesService implements IPokemonDataLoader {

    private final CsvPokemonSpeciesRepositoryImpl csvPokemonSpeciesRepository;
    private final PokemonSpeciesRepository pokemonSpeciesRepository;
    private final LegendaryRepository legendaryRepository;
    private final CsvEvolutionChainsRepositoryImpl csvEvolutionChainsRepositoryImpl;
    private final CsvPalParkRepositoryImpl csvPalParkRepositoryImpl;
    private final CsvPokemonDexNumbersRepositoryImpl csvPokemonDexNumbersRepositoryImpl;
    private final CsvPokemonEggGroupsRepositoryImpl csvPokemonEggGroupsRepositoryImpl;
    private final CsvPokemonEvolutionRepositoryImpl csvPokemonEvolutionRepositoryImpl;
    private final PalParkEncounterRepository palParkEncounterRepository;
    private final PokedexEntryRepository pokedexEntryRepository;

    public PokemonSpeciesService(CsvPokemonSpeciesRepositoryImpl csvPokemonSpeciesRepository,
                                 PokemonSpeciesRepository pokemonSpeciesRepository,
                                 LegendaryRepository legendaryRepository,
                                 CsvEvolutionChainsRepositoryImpl csvEvolutionChainsRepositoryImpl,
                                 CsvPalParkRepositoryImpl csvPalParkRepositoryImpl,
                                 CsvPokemonDexNumbersRepositoryImpl csvPokemonDexNumbersRepositoryImpl,
                                 CsvPokemonEggGroupsRepositoryImpl csvPokemonEggGroupsRepositoryImpl,
                                 CsvPokemonEvolutionRepositoryImpl csvPokemonEvolutionRepositoryImpl,
                                 PalParkEncounterRepository palParkEncounterRepository,
                                 PokedexEntryRepository pokedexEntryRepository) {
        this.csvPokemonSpeciesRepository = csvPokemonSpeciesRepository;
        this.pokemonSpeciesRepository = pokemonSpeciesRepository;
        this.legendaryRepository = legendaryRepository;
        this.csvEvolutionChainsRepositoryImpl = csvEvolutionChainsRepositoryImpl;
        this.csvPalParkRepositoryImpl = csvPalParkRepositoryImpl;
        this.csvPokemonDexNumbersRepositoryImpl = csvPokemonDexNumbersRepositoryImpl;
        this.csvPokemonEggGroupsRepositoryImpl = csvPokemonEggGroupsRepositoryImpl;
        this.csvPokemonEvolutionRepositoryImpl = csvPokemonEvolutionRepositoryImpl;
        this.palParkEncounterRepository = palParkEncounterRepository;
        this.pokedexEntryRepository = pokedexEntryRepository;
    }

    @Override
    public String getEntityName() { return "PokemonSpecies"; }

    @Override
    @Transactional
    public void loadNodes() {
        List<Long> legendaryIds = new ArrayList<>();

        List<Map<String, Object>> rows = csvPokemonSpeciesRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("name", csv.getName());
                    row.put("genus", csv.getGenus());
                    row.put("genderRate", csv.getGenderRate());
                    row.put("captureRate", csv.getCaptureRate());
                    row.put("baseHappiness", csv.getBaseHappiness());
                    row.put("isBaby", csv.getIsBaby() != 0);
                    row.put("isLegendary", csv.getIsLegendary() != 0);
                    row.put("isMythical", csv.getIsMythical() != 0);
                    row.put("hatchCounter", csv.getHatchCounter());
                    row.put("hasGenderDifferences", csv.getHasGenderDifferences() != 0);
                    row.put("formsSwitchable", csv.getFormsSwitchable() != 0);
                    row.put("order", csv.getOrder());
                    row.put("conquestOrder", csv.getConquestOrder());

                    if (csv.getIsLegendary() != 0) {
                        legendaryIds.add(csv.getId());
                    }

                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            Integer count = pokemonSpeciesRepository.batchInsertPokemonSpecies(rows);
            System.out.println("Successfully loaded " + count + " PokemonSpecies nodes.");
        }

        if (!legendaryIds.isEmpty()) {
            Integer count = legendaryRepository.batchInsertLegendary(legendaryIds);
            System.out.println("Successfully loaded " + count + " Legendary nodes.");
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvPokemonSpeciesRepository.getAll().forEach(csvPokemonSpecies -> {
            for (CsvPokemonEvolution csvPokemonEvolution : csvPokemonEvolutionRepositoryImpl.getByEvolvedSpeciesId(
                    csvPokemonSpecies.getId())) {
                pokemonSpeciesRepository.linkPokemonSpeciesToEvolutionStep(csvPokemonSpecies.getId(),
                        csvPokemonEvolution.getId());
            }

            csvEvolutionChainsRepositoryImpl.getAll().forEach(csvEvolutionChain -> {
                if (csvEvolutionChain.getId().equals(csvPokemonSpecies.getEvolutionChainId())) {
                    pokemonSpeciesRepository.linkPokemonSpeciesIsPartOfEvolutionChain(csvPokemonSpecies.getId(), csvEvolutionChain.getId());
                }
            });

            if (csvPokemonSpecies.getEvolvesFromSpeciesId() != null) {
                pokemonSpeciesRepository.linkPokemonSpeciesToPokemonSpecies(csvPokemonSpecies.getId(), csvPokemonSpecies.getEvolvesFromSpeciesId());
            }

            if (csvPokemonSpecies.getColorId() != null) {
                pokemonSpeciesRepository.linkPokemonSpeciesToPokemonColor(csvPokemonSpecies.getId(), csvPokemonSpecies.getColorId());
            }

            if (csvPokemonSpecies.getGenerationId() != null) {
                pokemonSpeciesRepository.linkPokemonSpeciesToGeneration(csvPokemonSpecies.getId(), csvPokemonSpecies.getGenerationId());
            }

            if (csvPokemonSpecies.getShapeId() != null) {
                pokemonSpeciesRepository.linkPokemonSpeciesToPokemonShape(csvPokemonSpecies.getId(), csvPokemonSpecies.getShapeId());
            }

            if (csvPokemonSpecies.getHabitatId() != null) {
                pokemonSpeciesRepository.linkPokemonSpeciesToPokemonHabitat(csvPokemonSpecies.getId(), csvPokemonSpecies.getHabitatId());
            }

            if (csvPokemonSpecies.getGrowthRateId() != null) {
                pokemonSpeciesRepository.linkPokemonSpeciesToGrowthRate(csvPokemonSpecies.getId(), csvPokemonSpecies.getGrowthRateId());
            }

            csvPalParkRepositoryImpl.getAll().forEach(csvPalPark -> {
                if(csvPalPark.getSpeciesId().equals(csvPokemonSpecies.getId())) {
                    palParkEncounterRepository.linkPokemonSpeciesToPalParkArea(csvPokemonSpecies.getId(),
                            csvPalPark.getAreaId(), csvPalPark.getBaseScore(), csvPalPark.getRate());
                }
            });

            for (CsvPokemonDexNumbers csvPokemonDexNumber : csvPokemonDexNumbersRepositoryImpl.getBySpeciesId(
                    csvPokemonSpecies.getId())) {
                pokedexEntryRepository.linkPokemonSpeciesToPokedex(csvPokemonSpecies.getId(),
                        csvPokemonDexNumber.getPokedexId(), csvPokemonDexNumber.getPokedexNumber());
            }

            csvPokemonEggGroupsRepositoryImpl.getAll().forEach(csvPokemonEggGroup -> {
                if (csvPokemonEggGroup.getSpeciesId().equals(csvPokemonSpecies.getId())) {
                    pokemonSpeciesRepository.linkPokemonSpeciesToEggGroup(csvPokemonSpecies.getId(), csvPokemonEggGroup.getEggGroupId());
                }
            });
        });
    }
}
