package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvItem;
import at.jku.faw.neo4jdemo.model.csv.CsvItemFlagMap;
import at.jku.faw.neo4jdemo.model.csv.CsvItemGameIndices;
import at.jku.faw.neo4jdemo.model.csv.CsvMachine;
import at.jku.faw.neo4jdemo.model.neo4j.Machine;
import at.jku.faw.neo4jdemo.repository.csv.CsvItemFlagMapRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvItemGameIndicesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvItemRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvMachineRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.GameIndexRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.ItemRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.MachineRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemService implements IPokemonDataLoader {

    private final CsvItemRepositoryImpl csvMainRepo;
    private final ItemRepository itemRepository;
    private final CsvItemFlagMapRepositoryImpl csvItemFlagMapRepositoryImpl;
    private final CsvMachineRepositoryImpl csvMachineRepositoryImpl;
    private final MachineRepository machineRepository;
    private final CsvItemGameIndicesRepositoryImpl csvItemGameIndicesRepositoryImpl;
	private final GameIndexRepository gameIndexRepository;

	public ItemService(CsvItemRepositoryImpl csvMainRepo,
					   ItemRepository neo4jRepo,
					   CsvItemFlagMapRepositoryImpl csvItemFlagMapRepositoryImpl,
					   CsvMachineRepositoryImpl csvMachineRepositoryImpl, MachineRepository machineRepository,
					   CsvItemGameIndicesRepositoryImpl csvItemGameIndicesRepositoryImpl,
					   GameIndexRepository gameIndexRepository) {
        this.csvMainRepo = csvMainRepo;
        this.itemRepository = neo4jRepo;
        this.csvItemFlagMapRepositoryImpl = csvItemFlagMapRepositoryImpl;
        this.csvMachineRepositoryImpl = csvMachineRepositoryImpl;
        this.machineRepository = machineRepository;
        this.csvItemGameIndicesRepositoryImpl = csvItemGameIndicesRepositoryImpl;
		this.gameIndexRepository = gameIndexRepository;
	}

    @Override
    public String getEntityName() { return "Item"; }

    @Override
    @Transactional
    public void loadNodes() {
		List<Map<String, Object>> rows = csvMainRepo.getAll().stream()
				.map(csvItem -> {
					Map<String, Object> row = new java.util.HashMap<>();
					row.put("id", csvItem.getId());
					row.put("identifier", csvItem.getIdentifier());
					row.put("name", csvItem.getName());
					row.put("cost", csvItem.getCost());
					row.put("flingPower", csvItem.getFlingPower());
					row.put("shortEffect", csvItem.getShortEffect());
					row.put("effect", csvItem.getEffect());
					return row;
				})
				.collect(Collectors.toList());

		if (!rows.isEmpty()) {
			itemRepository.batchInsertItems(rows);
		}
	}

    @Override
    @Transactional
    public void loadRelationships() {
		Map<Long, List<CsvItemFlagMap>> flagMap = csvItemFlagMapRepositoryImpl.getAll().stream()
				.collect(Collectors.groupingBy(CsvItemFlagMap::getItemId));

		Map<Long, List<CsvItemGameIndices>> gameIndexMap = csvItemGameIndicesRepositoryImpl.getAll().stream()
				.collect(Collectors.groupingBy(CsvItemGameIndices::getItemId));

		Map<Long, List<CsvMachine>> machineCsvMap = csvMachineRepositoryImpl.getAll().stream()
				.collect(Collectors.groupingBy(CsvMachine::getItemId));

		Map<Integer, Long> machineLookup = machineRepository.findAll().stream()
				.collect(Collectors.toMap(
						Machine::getMachineNumber,
						Machine::getId
				));

		for (CsvItem csvItem : csvMainRepo.getAll()) {
			Long itemId = csvItem.getId();

			// Category
			if (csvItem.getCategoryId() != null) {
				itemRepository.linkItemToItemCategory(itemId, csvItem.getCategoryId());
			}

			// Fling Effect
			if (csvItem.getFlingEffectId() != null) {
				itemRepository.linkItemToFlingEffect(itemId, csvItem.getFlingEffectId());
			}

			// Item Flags
			List<CsvItemFlagMap> flags = flagMap.get(itemId);
			if (flags != null) {
				flags.forEach(f -> itemRepository.linkItemToItemFlag(itemId, f.getItemFlagId()));
			}

			// Game Indices
			List<CsvItemGameIndices> indices = gameIndexMap.get(itemId);
			if (indices != null) {
				indices.forEach(idx -> gameIndexRepository.linkItemHasGameIndex(
						itemId, idx.getGenerationId(), idx.getGameIndex()));
			}

			// Machines
			List<CsvMachine> machines = machineCsvMap.get(itemId);
			if (machines != null) {
				for (CsvMachine csvMachine : machines) {
					Long generatedMachineId = machineLookup.get(csvMachine.getMachineNumber());
					if (generatedMachineId != null) {
						itemRepository.linkItemToMachine(itemId, generatedMachineId);
					}
				}
			}
		}
	}
}
