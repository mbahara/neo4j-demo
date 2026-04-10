package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvPokemon;
import at.jku.faw.neo4jdemo.model.neo4j.HeldItem;
import at.jku.faw.neo4jdemo.model.neo4j.Pokemon;
import at.jku.faw.neo4jdemo.repository.csv.CsvEncountersRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonAbilitiesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonGameIndicesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonItemsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonMovesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonStatsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonTypeRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.HeldItemRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.PokemonRepository;
import at.jku.faw.neo4jdemo.service.IDataLoader;
import at.jku.faw.neo4jdemo.utils.CsvUtils;
import org.springframework.stereotype.Service;

@Service
public class PokemonService implements IDataLoader {

	private final CsvPokemonRepositoryImpl csvPokemonRepository;
	private final PokemonRepository pokemonRepository;

	private final CsvPokemonStatsRepositoryImpl csvPokemonStatsRepository;
	private final CsvPokemonItemsRepositoryImpl csvPokemonItemsRepository;
	private final HeldItemRepository heldItemRepository;
	private final CsvPokemonAbilitiesRepositoryImpl csvPokemonAbilitiesRepositoryImpl;
	private final CsvPokemonGameIndicesRepositoryImpl csvPokemonGameIndicesRepositoryImpl;
	private final CsvPokemonMovesRepositoryImpl csvPokemonMovesRepositoryImpl;
	private final CsvEncountersRepositoryImpl csvEncountersRepositoryImpl;
	private final CsvPokemonTypeRepositoryImpl csvPokemonTypeRepositoryImpl;

	public PokemonService(CsvPokemonRepositoryImpl csvPokemonRepository, PokemonRepository pokemonRepository,
						  CsvPokemonStatsRepositoryImpl csvPokemonStatsRepository,
						  HeldItemRepository heldItemRepository,
						  CsvPokemonItemsRepositoryImpl csvPokemonItemsRepository,
						  CsvPokemonAbilitiesRepositoryImpl csvPokemonAbilitiesRepositoryImpl,
						  CsvPokemonGameIndicesRepositoryImpl csvPokemonGameIndicesRepositoryImpl,
						  CsvPokemonMovesRepositoryImpl csvPokemonMovesRepositoryImpl,
						  CsvEncountersRepositoryImpl csvEncountersRepositoryImpl,
						  CsvPokemonTypeRepositoryImpl csvPokemonTypeRepositoryImpl) {
		this.csvPokemonRepository = csvPokemonRepository;
		this.pokemonRepository = pokemonRepository;
		this.csvPokemonStatsRepository = csvPokemonStatsRepository;
		this.heldItemRepository = heldItemRepository;
		this.csvPokemonItemsRepository = csvPokemonItemsRepository;
		this.csvPokemonAbilitiesRepositoryImpl = csvPokemonAbilitiesRepositoryImpl;
		this.csvPokemonGameIndicesRepositoryImpl = csvPokemonGameIndicesRepositoryImpl;
		this.csvPokemonMovesRepositoryImpl = csvPokemonMovesRepositoryImpl;
		this.csvEncountersRepositoryImpl = csvEncountersRepositoryImpl;
		this.csvPokemonTypeRepositoryImpl = csvPokemonTypeRepositoryImpl;
	}

	@Override
	public void loadNodes() {
		csvPokemonRepository.getAll().forEach(csvPokemon -> {
			getPokemonById(csvPokemon.id());
		});
	}

	@Override
	public void loadRelationships() {
		csvPokemonRepository.getAll().forEach(csvPokemon -> {
			Long pokemonId = csvPokemon.id();

			// Simple Node Link
			pokemonRepository.linkPokemonToPokemonSpecies(pokemonId, csvPokemon.speciesId());
			// constraint => moves must exist
			csvPokemonMovesRepositoryImpl.getByPokemonId(pokemonId).forEach(pokemonMove -> {
				pokemonRepository.linkPokemonToPokemonMove(pokemonId, pokemonMove.moveId());
			});
			// constraint => encounters exist
			csvEncountersRepositoryImpl.getByPokemonId(pokemonId).forEach(csvEncounter -> {
				pokemonRepository.linkPokemonToEncounter(pokemonId, csvEncounter.id());
			});

			// Propertied Relationship Link
			// constraint => stats must exist
			csvPokemonStatsRepository.getByPokemonId(pokemonId).forEach(pokemonStats -> {
				pokemonRepository.linkPokemonToStats(pokemonId, pokemonStats.statId(), pokemonStats.baseStat(), pokemonStats.effort());
			});
			// constraint => items must exist
			csvPokemonItemsRepository.getByPokemonId(pokemonId).forEach(pokemonItem -> {
				HeldItem heldItem = heldItemRepository.insertHeldItem(pokemonItem.rarity());
				pokemonRepository.linkPokemonToHeldItem(pokemonId, heldItem.getId());
				heldItemRepository.linkHeldItemToItem(heldItem.getId(), pokemonItem.itemId());
				heldItemRepository.linkHeldItemToVersion(heldItem.getId(), pokemonItem.versionId());
			});
			// constraint => abilities must exist
			csvPokemonAbilitiesRepositoryImpl.getByPokemonId(pokemonId).forEach(pokemonAbility -> {
				pokemonRepository.linkPokemonToAbility(pokemonId, pokemonAbility.abilityId(), Boolean.TRUE.equals(CsvUtils.extractBoolean(pokemonAbility.isHidden())),
						pokemonAbility.slot());
			});
			// constraint => versions must exist
			csvPokemonGameIndicesRepositoryImpl.getByPokemonId(pokemonId).forEach(pokemonGameIndex -> {
				pokemonRepository.linkPokemonHasGameIndex(pokemonId, pokemonGameIndex.versionId(), pokemonGameIndex.gameIndex());
			});
			// constraint => type must exist
			csvPokemonTypeRepositoryImpl.getByPokemonId(pokemonId).forEach(pokemonType -> {
				pokemonRepository.linkPokemonToType(pokemonId, pokemonType.typeId(), pokemonType.slot());
			});

		});
	}

	@Override
	public String getEntityName() {
		return "Pokemon";
	}

	private void getPokemonById(Long id) {
		pokemonRepository.findById(id).orElseGet(() -> {
			Pokemon pokemon = buildPokemonByDexId(id);
			pokemonRepository.save(pokemon);
			return pokemon;
		});
	}

	private Pokemon buildPokemonByDexId(long id) {
		CsvPokemon csvPokemon = csvPokemonRepository.getById(id);

		return pokemonRepository.insertPokemon(
				csvPokemon.id(),
				csvPokemon.identifier(),
				csvPokemon.height(),
				csvPokemon.weight(),
				csvPokemon.baseExperience(),
				csvPokemon.order(),
				Boolean.TRUE.equals(CsvUtils.extractBoolean(csvPokemon.isDefault()))
		);
	}
}
