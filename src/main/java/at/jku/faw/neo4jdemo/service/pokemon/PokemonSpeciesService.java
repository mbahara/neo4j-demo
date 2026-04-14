package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvPokemonDexNumbers;
import at.jku.faw.neo4jdemo.model.csv.CsvPokemonEvolution;
import at.jku.faw.neo4jdemo.model.neo4j.PokemonSpecies;
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
        csvPokemonSpeciesRepository.getAll().forEach(csv -> {
            PokemonSpecies species = pokemonSpeciesRepository.insertPokemonSpecies(csv.id(), csv.name(),
                    csv.genus(), csv.genderRate(), csv.captureRate(), csv.baseHappiness(), csv.isBaby() != 0,
                    csv.isLegendary() != 0, csv.isMythical()  != 0, csv.hatchCounter(),
                    csv.hasGenderDifferences() != 0, csv.formsSwitchable() != 0,
                    csv.order(), csv.conquestOrder());
            if (species.isLegendary()) {
                legendaryRepository.insertLegendary(species.getId());
            }
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvPokemonSpeciesRepository.getAll().forEach(csvPokemonSpecies -> {
			for (CsvPokemonEvolution csvPokemonEvolution : csvPokemonEvolutionRepositoryImpl.getByEvolvedSpeciesId(
					csvPokemonSpecies.id())) {
				pokemonSpeciesRepository.linkPokemonSpeciesToEvolutionStep(csvPokemonSpecies.id(),
						csvPokemonEvolution.id());
			}

			csvEvolutionChainsRepositoryImpl.getAll().forEach(csvEvolutionChain -> {
                if (csvEvolutionChain.id().equals(csvPokemonSpecies.evolutionChainId())) {
                    pokemonSpeciesRepository.linkPokemonSpeciesIsPartOfEvolutionChain(csvPokemonSpecies.id(), csvEvolutionChain.id());
                }
            });

            if (csvPokemonSpecies.evolvesFromSpeciesId() != null) {
                pokemonSpeciesRepository.linkPokemonSpeciesToPokemonSpecies(csvPokemonSpecies.id(), csvPokemonSpecies.evolvesFromSpeciesId());
            }

            if (csvPokemonSpecies.colorId() != null) {
                pokemonSpeciesRepository.linkPokemonSpeciesToPokemonColor(csvPokemonSpecies.id(), csvPokemonSpecies.colorId());
            }

            if (csvPokemonSpecies.generationId() != null) {
                pokemonSpeciesRepository.linkPokemonSpeciesToGeneration(csvPokemonSpecies.id(), csvPokemonSpecies.generationId());
            }

            if (csvPokemonSpecies.shapeId() != null) {
                pokemonSpeciesRepository.linkPokemonSpeciesToPokemonShape(csvPokemonSpecies.id(), csvPokemonSpecies.shapeId());
            }

            if (csvPokemonSpecies.habitatId() != null) {
                pokemonSpeciesRepository.linkPokemonSpeciesToPokemonHabitat(csvPokemonSpecies.id(), csvPokemonSpecies.habitatId());
            }

            if (csvPokemonSpecies.growthRateId() != null) {
                pokemonSpeciesRepository.linkPokemonSpeciesToGrowthRate(csvPokemonSpecies.id(), csvPokemonSpecies.growthRateId());
            }

            csvPalParkRepositoryImpl.getAll().forEach(csvPalPark -> {
                if(csvPalPark.speciesId().equals(csvPokemonSpecies.id())) {
                    palParkEncounterRepository.linkPokemonSpeciesToPalParkArea(csvPokemonSpecies.id(),
                            csvPalPark.areaId(), csvPalPark.baseScore(), csvPalPark.rate());
                }
            });

			for (CsvPokemonDexNumbers csvPokemonDexNumber : csvPokemonDexNumbersRepositoryImpl.getBySpeciesId(
					csvPokemonSpecies.id())) {
				pokedexEntryRepository.linkPokemonSpeciesToPokedex(csvPokemonSpecies.id(),
						csvPokemonDexNumber.pokedexId(), csvPokemonDexNumber.pokedexNumber());
			}

			csvPokemonEggGroupsRepositoryImpl.getAll().forEach(csvPokemonEggGroup -> {
                if (csvPokemonEggGroup.speciesId().equals(csvPokemonSpecies.id())) {
                    pokemonSpeciesRepository.linkPokemonSpeciesToEggGroup(csvPokemonSpecies.id(), csvPokemonEggGroup.eggGroupId());
                }
            });
        });
    }
}
