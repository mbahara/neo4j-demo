package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.neo4j.HeldItem;
import at.jku.faw.neo4jdemo.repository.csv.CsvEncountersRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonAbilitiesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonGameIndicesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonItemsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonMovesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonStatsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonTypeRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.HasStatsRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.HeldItemRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.PokemonAbilityRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.PokemonGameIndexRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.PokemonRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.PokemonTypeRepository;
import at.jku.faw.neo4jdemo.utils.CsvUtils;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PokemonService implements IPokemonDataLoader {

    private final CsvPokemonRepositoryImpl csvPokemonRepository;
    private final PokemonRepository pokemonRepository;
    private final CsvPokemonStatsRepositoryImpl csvPokemonStatsRepositoryImpl;
    private final CsvPokemonTypeRepositoryImpl csvPokemonTypeRepositoryImpl;
    private final CsvPokemonItemsRepositoryImpl csvPokemonItemsRepository;
    private final HeldItemRepository heldItemRepository;
    private final CsvPokemonAbilitiesRepositoryImpl csvPokemonAbilitiesRepositoryImpl;
    private final CsvPokemonGameIndicesRepositoryImpl csvPokemonGameIndicesRepositoryImpl;
    private final CsvPokemonMovesRepositoryImpl csvPokemonMovesRepositoryImpl;
    private final CsvEncountersRepositoryImpl csvEncountersRepositoryImpl;
	private final HasStatsRepository hasStatsRepository;
	private final PokemonAbilityRepository pokemonAbilityRepository;
	private final PokemonTypeRepository pokemonTypeRepository;
	private final PokemonGameIndexRepository pokemonGameIndexRepository;


	public PokemonService(CsvPokemonRepositoryImpl csvMainRepo,
						  PokemonRepository neo4jRepo,
						  CsvPokemonStatsRepositoryImpl csvPokemonStatsRepositoryImpl,
						  CsvPokemonTypeRepositoryImpl csvPokemonTypeRepositoryImpl,
						  CsvPokemonItemsRepositoryImpl csvPokemonItemsRepository,
						  HeldItemRepository heldItemRepository,
						  CsvPokemonAbilitiesRepositoryImpl csvPokemonAbilitiesRepositoryImpl,
						  CsvPokemonGameIndicesRepositoryImpl csvPokemonGameIndicesRepositoryImpl,
						  CsvPokemonMovesRepositoryImpl csvPokemonMovesRepositoryImpl,
						  CsvEncountersRepositoryImpl csvEncountersRepositoryImpl,
						  HasStatsRepository hasStatsRepository, PokemonAbilityRepository pokemonAbilityRepository,
						  PokemonTypeRepository pokemonTypeRepository,
						  PokemonGameIndexRepository pokemonGameIndexRepository) {
        this.csvPokemonRepository = csvMainRepo;
        this.pokemonRepository = neo4jRepo;
        this.csvPokemonStatsRepositoryImpl = csvPokemonStatsRepositoryImpl;
        this.csvPokemonTypeRepositoryImpl = csvPokemonTypeRepositoryImpl;
		this.csvPokemonItemsRepository = csvPokemonItemsRepository;
		this.heldItemRepository = heldItemRepository;
		this.csvPokemonAbilitiesRepositoryImpl = csvPokemonAbilitiesRepositoryImpl;
		this.csvPokemonGameIndicesRepositoryImpl = csvPokemonGameIndicesRepositoryImpl;
		this.csvPokemonMovesRepositoryImpl = csvPokemonMovesRepositoryImpl;
		this.csvEncountersRepositoryImpl = csvEncountersRepositoryImpl;
		this.hasStatsRepository = hasStatsRepository;
		this.pokemonAbilityRepository = pokemonAbilityRepository;
		this.pokemonTypeRepository = pokemonTypeRepository;
		this.pokemonGameIndexRepository = pokemonGameIndexRepository;
	}

    @Override
    public String getEntityName() { return "Pokemon"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvPokemonRepository.getAll().forEach(csv -> {
            pokemonRepository.insertPokemon(csv.getId(), csv.getIdentifier(),
					csv.getHeight(), csv.getWeight(), csv.getBaseExperience(),
					csv.getOrder(), csv.getIsDefault() != 0);
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvPokemonRepository.getAll().forEach(csvPokemon -> {
			// Simple Node Link
			pokemonRepository.linkPokemonToPokemonSpecies(csvPokemon.getId(), csvPokemon.getSpeciesId());

			csvPokemonTypeRepositoryImpl.getAll().stream()
					.filter(csvPokemonType ->
							Objects.equals(csvPokemonType.getPokemonId(), csvPokemon.getId()))
					.forEach(csvPokemonType ->
							pokemonTypeRepository.linkPokemonToType(csvPokemon.getId(),
									csvPokemonType.getTypeId(), csvPokemonType.getSlot()));

			// Propertied Relationship Link
			csvPokemonStatsRepositoryImpl.getAll().stream()
                .filter(m -> Objects.equals(m.getPokemonId(), csvPokemon.getId()))
                .forEach(stats ->
						hasStatsRepository.linkPokemonToStats(csvPokemon.getId(),
								stats.getStatId(), stats.getBaseStat(), stats.getEffort()));

            csvPokemonItemsRepository.getAll().stream()
                    .filter(m -> Objects.equals(m.getPokemonId(), csvPokemon.getId()))
                    .forEach(item -> {
                        HeldItem heldItem = heldItemRepository.insertHeldItem(item.getRarity());
                        pokemonRepository.linkPokemonToHeldItem(item.getPokemonId(), heldItem.getId());
						heldItemRepository.linkHeldItemToItem(heldItem.getId(), item.getItemId());
						heldItemRepository.linkHeldItemToVersion(heldItem.getId(), item.getVersionId());
                    });

			csvPokemonAbilitiesRepositoryImpl.getAll().stream()
					.filter(csvPokemonAbilities -> Objects.equals(csvPokemonAbilities.getPokemonId(), csvPokemon.getId()))
					.forEach(abilities ->
						pokemonAbilityRepository.linkPokemonToAbility(abilities.getPokemonId(), abilities.getAbilityId(), Boolean.TRUE.equals(
								CsvUtils.extractBoolean(abilities.getIsHidden())), abilities.getSlot()));
            csvPokemonGameIndicesRepositoryImpl.getAll().stream()
					.filter(gi -> Objects.equals(gi.getPokemonId(), csvPokemon.getId()))
					.forEach(index -> {
						pokemonGameIndexRepository.linkPokemonHasGameIndex(index.getPokemonId(), index.getVersionId(), index.getGameIndex());
					});
			csvPokemonMovesRepositoryImpl.getAll().stream()
					.filter(pokemonMove ->
							Objects.equals(pokemonMove.getPokemonId(), csvPokemon.getId()))
					.forEach(pokemonMove ->
							pokemonRepository.linkPokemonToPokemonMove(pokemonMove.getPokemonId(),
									pokemonMove.getMoveId()));

			csvEncountersRepositoryImpl.getAll().stream()
					.filter(csvEncounter ->
							Objects.equals(csvEncounter.getPokemonId(), csvPokemon.getId()))
					.forEach(csvEncounter ->
							pokemonRepository.linkPokemonToEncounter(csvEncounter.getPokemonId(), csvEncounter.getId()));
        });
    }
}
