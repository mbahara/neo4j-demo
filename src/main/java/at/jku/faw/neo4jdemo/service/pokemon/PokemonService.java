package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvEncounters;
import at.jku.faw.neo4jdemo.model.csv.CsvPokemonAbilities;
import at.jku.faw.neo4jdemo.model.csv.CsvPokemonGameIndices;
import at.jku.faw.neo4jdemo.model.csv.CsvPokemonItems;
import at.jku.faw.neo4jdemo.model.csv.CsvPokemonMoves;
import at.jku.faw.neo4jdemo.model.csv.CsvPokemonStats;
import at.jku.faw.neo4jdemo.model.csv.CsvPokemonType;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        List<Map<String, Object>> rows = csvPokemonRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("identifier", csv.getIdentifier());
                    row.put("height", csv.getHeight());
                    row.put("weight", csv.getWeight());
                    row.put("baseExperience", csv.getBaseExperience());
                    row.put("order", csv.getOrder());
                    row.put("isDefault", csv.getIsDefault() != 0);
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            Integer count = pokemonRepository.batchInsertPokemons(rows);
            System.out.println("Successfully loaded " + count + " Pokemons nodes.");
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
		Map<Long, List<CsvPokemonType>> typeMap = csvPokemonTypeRepositoryImpl.getAll().stream()
				.collect(Collectors.groupingBy(CsvPokemonType::getPokemonId));

		Map<Long, List<CsvPokemonStats>> statsMap = csvPokemonStatsRepositoryImpl.getAll().stream()
				.collect(Collectors.groupingBy(CsvPokemonStats::getPokemonId));

		Map<Long, List<CsvPokemonItems>> itemMap = csvPokemonItemsRepository.getAll().stream()
				.collect(Collectors.groupingBy(CsvPokemonItems::getPokemonId));

		Map<Long, List<CsvPokemonAbilities>> abilityMap = csvPokemonAbilitiesRepositoryImpl.getAll().stream()
				.collect(Collectors.groupingBy(CsvPokemonAbilities::getPokemonId));

		Map<Long, List<CsvPokemonGameIndices>> giMap = csvPokemonGameIndicesRepositoryImpl.getAll().stream()
				.collect(Collectors.groupingBy(CsvPokemonGameIndices::getPokemonId));

		Map<Long, List<CsvPokemonMoves>> moveMap = csvPokemonMovesRepositoryImpl.getAll().stream()
				.collect(Collectors.groupingBy(CsvPokemonMoves::getPokemonId));

		Map<Long, List<CsvEncounters>> encounterMap = csvEncountersRepositoryImpl.getAll().stream()
				.collect(Collectors.groupingBy(CsvEncounters::getPokemonId));

		csvPokemonRepository.getAll().forEach(csv -> {
			Long pId = csv.getId();
			pokemonRepository.linkPokemonToPokemonSpecies(pId, csv.getSpeciesId());

			if (typeMap.containsKey(pId)) {
				typeMap.get(pId).forEach(t -> pokemonTypeRepository.linkPokemonToType(pId, t.getTypeId(), t.getSlot()));
			}

			if (statsMap.containsKey(pId)) {
				statsMap.get(pId).forEach(s -> hasStatsRepository.linkPokemonToStats(pId, s.getStatId(), s.getBaseStat(), s.getEffort()));
			}

			if (abilityMap.containsKey(pId)) {
				abilityMap.get(pId).forEach(a -> pokemonAbilityRepository.linkPokemonToAbility(pId, a.getAbilityId(),
						Boolean.TRUE.equals(CsvUtils.extractBoolean(a.getIsHidden())), a.getSlot()));
			}

			if (giMap.containsKey(pId)) {
				giMap.get(pId).forEach(index -> pokemonGameIndexRepository.linkPokemonHasGameIndex(pId, index.getVersionId(), index.getGameIndex()));
			}

			if (moveMap.containsKey(pId)) {
				moveMap.get(pId).forEach(m -> pokemonRepository.linkPokemonToPokemonMove(pId, m.getMoveId()));
			}

			if (encounterMap.containsKey(pId)) {
				encounterMap.get(pId).forEach(e -> pokemonRepository.linkPokemonToEncounter(pId, e.getId()));
			}

			if (itemMap.containsKey(pId)) {
				itemMap.get(pId).forEach(item -> {
					HeldItem heldItem = heldItemRepository.insertHeldItem(item.getRarity());
					pokemonRepository.linkPokemonToHeldItem(pId, heldItem.getId());
					heldItemRepository.linkHeldItemToItem(heldItem.getId(), item.getItemId());
					heldItemRepository.linkHeldItemToVersion(heldItem.getId(), item.getVersionId());
				});
			}
		});
    }
}
