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
import at.jku.faw.neo4jdemo.repository.neo4j.HeldItemRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.PokemonRepository;
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


    public PokemonService(CsvPokemonRepositoryImpl csvMainRepo,
						  PokemonRepository neo4jRepo,
						  CsvPokemonStatsRepositoryImpl csvPokemonStatsRepositoryImpl,
						  CsvPokemonTypeRepositoryImpl csvPokemonTypeRepositoryImpl,
						  CsvPokemonItemsRepositoryImpl csvPokemonItemsRepository,
						  HeldItemRepository heldItemRepository,
						  CsvPokemonAbilitiesRepositoryImpl csvPokemonAbilitiesRepositoryImpl,
						  CsvPokemonGameIndicesRepositoryImpl csvPokemonGameIndicesRepositoryImpl,
						  CsvPokemonMovesRepositoryImpl csvPokemonMovesRepositoryImpl,
						  CsvEncountersRepositoryImpl csvEncountersRepositoryImpl) {
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
	}

    @Override
    public String getEntityName() { return "Pokemon"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvPokemonRepository.getAll().forEach(csv -> {
            pokemonRepository.insertPokemon(csv.id(), csv.identifier(),
					csv.height(), csv.weight(), csv.baseExperience(),
					csv.order(), csv.isDefault() != 0);
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvPokemonRepository.getAll().forEach(csvPokemon -> {
			// Simple Node Link
			pokemonRepository.linkPokemonToPokemonSpecies(csvPokemon.id(), csvPokemon.speciesId());

			csvPokemonTypeRepositoryImpl.getAll().stream()
					.filter(csvPokemonType ->
							Objects.equals(csvPokemonType.pokemonId(), csvPokemon.id()))
					.forEach(csvPokemonType ->
							pokemonRepository.linkPokemonToType(csvPokemon.id(),
									csvPokemonType.typeId(), csvPokemonType.slot()));

			// Propertied Relationship Link
			csvPokemonStatsRepositoryImpl.getAll().stream()
                .filter(m -> Objects.equals(m.pokemonId(), csvPokemon.id()))
                .forEach(stats ->
						pokemonRepository.linkPokemonToStats(csvPokemon.id(),
								stats.statId(), stats.baseStat(), stats.effort()));

            csvPokemonItemsRepository.getAll().stream()
                    .filter(m -> Objects.equals(m.pokemonId(), csvPokemon.id()))
                    .forEach(item -> {
                        HeldItem heldItem = heldItemRepository.insertHeldItem(item.rarity());
                        pokemonRepository.linkPokemonToHeldItem(item.pokemonId(), heldItem.getId());
						heldItemRepository.linkHeldItemToItem(heldItem.getId(), item.itemId());
						heldItemRepository.linkHeldItemToVersion(heldItem.getId(), item.versionId());
                    });

			csvPokemonAbilitiesRepositoryImpl.getAll().stream()
					.filter(csvPokemonAbilities -> Objects.equals(csvPokemonAbilities.pokemonId(), csvPokemon.id()))
					.forEach(abilities ->
						pokemonRepository.linkPokemonToAbility(abilities.pokemonId(), abilities.abilityId(), Boolean.TRUE.equals(
								CsvUtils.extractBoolean(abilities.isHidden())), abilities.slot()));
            csvPokemonGameIndicesRepositoryImpl.getAll().stream()
					.filter(gi -> Objects.equals(gi.pokemonId(), csvPokemon.id()))
					.forEach(index -> {
						pokemonRepository.linkPokemonHasGameIndex(index.pokemonId(), index.versionId(), index.gameIndex());
					});
			csvPokemonMovesRepositoryImpl.getAll().stream()
					.filter(pokemonMove ->
							Objects.equals(pokemonMove.pokemonId(), csvPokemon.id()))
					.forEach(pokemonMove ->
							pokemonRepository.linkPokemonToPokemonMove(pokemonMove.pokemonId(),
									pokemonMove.moveId()));

			csvEncountersRepositoryImpl.getAll().stream()
					.filter(csvEncounter ->
							Objects.equals(csvEncounter.pokemonId(), csvPokemon.id()))
					.forEach(csvEncounter ->
							pokemonRepository.linkPokemonToEncounter(csvEncounter.pokemonId(), csvEncounter.id()));
        });
    }
}
