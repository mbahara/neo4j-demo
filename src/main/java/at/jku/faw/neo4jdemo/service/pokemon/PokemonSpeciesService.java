package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvEvolutionChains;
import at.jku.faw.neo4jdemo.model.csv.CsvPalPark;
import at.jku.faw.neo4jdemo.model.csv.CsvPokemonDexNumbers;
import at.jku.faw.neo4jdemo.model.csv.CsvPokemonEggGroups;
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
        Map<Long, List<CsvPokemonEvolution>> evolutionMap = csvPokemonEvolutionRepositoryImpl.getAll().stream()
                .collect(Collectors.groupingBy(CsvPokemonEvolution::getEvolvedSpeciesId));

        Map<Long, List<CsvPalPark>> palParkMap = csvPalParkRepositoryImpl.getAll().stream()
                .collect(Collectors.groupingBy(CsvPalPark::getSpeciesId));

        Map<Long, List<CsvPokemonDexNumbers>> dexMap = csvPokemonDexNumbersRepositoryImpl.getAll().stream()
                .collect(Collectors.groupingBy(CsvPokemonDexNumbers::getSpeciesId));

        Map<Long, List<CsvPokemonEggGroups>> eggGroupMap = csvPokemonEggGroupsRepositoryImpl.getAll().stream()
                .collect(Collectors.groupingBy(CsvPokemonEggGroups::getSpeciesId));

        Map<Long, CsvEvolutionChains> chainMap = csvEvolutionChainsRepositoryImpl.getAll().stream()
                .collect(Collectors.toMap(CsvEvolutionChains::getId, c -> c, (existing, replacement) -> existing));

        csvPokemonSpeciesRepository.getAll().forEach(csv -> {
            Long sId = csv.getId();

            if (evolutionMap.containsKey(sId)) {
                evolutionMap.get(sId).forEach(evo ->
                        pokemonSpeciesRepository.linkPokemonSpeciesToEvolutionStep(sId, evo.getId()));
            }

            if (csv.getEvolutionChainId() != null && chainMap.containsKey(csv.getEvolutionChainId())) {
                pokemonSpeciesRepository.linkPokemonSpeciesIsPartOfEvolutionChain(sId, csv.getEvolutionChainId());
            }

            if (csv.getEvolvesFromSpeciesId() != null) pokemonSpeciesRepository.linkPokemonSpeciesToPokemonSpecies(sId, csv.getEvolvesFromSpeciesId());
            if (csv.getColorId() != null) pokemonSpeciesRepository.linkPokemonSpeciesToPokemonColor(sId, csv.getColorId());
            if (csv.getGenerationId() != null) pokemonSpeciesRepository.linkPokemonSpeciesToGeneration(sId, csv.getGenerationId());
            if (csv.getShapeId() != null) pokemonSpeciesRepository.linkPokemonSpeciesToPokemonShape(sId, csv.getShapeId());
            if (csv.getHabitatId() != null) pokemonSpeciesRepository.linkPokemonSpeciesToPokemonHabitat(sId, csv.getHabitatId());
            if (csv.getGrowthRateId() != null) pokemonSpeciesRepository.linkPokemonSpeciesToGrowthRate(sId, csv.getGrowthRateId());

            if (palParkMap.containsKey(sId)) {
                palParkMap.get(sId).forEach(pp ->
                        palParkEncounterRepository.linkPokemonSpeciesToPalParkArea(sId, pp.getAreaId(), pp.getBaseScore(), pp.getRate()));
            }

            if (dexMap.containsKey(sId)) {
                dexMap.get(sId).forEach(dn ->
                        pokedexEntryRepository.linkPokemonSpeciesToPokedex(sId, dn.getPokedexId(), dn.getPokedexNumber()));
            }

            if (eggGroupMap.containsKey(sId)) {
                eggGroupMap.get(sId).forEach(eg ->
                        pokemonSpeciesRepository.linkPokemonSpeciesToEggGroup(sId, eg.getEggGroupId()));
            }
        });
    }
}
