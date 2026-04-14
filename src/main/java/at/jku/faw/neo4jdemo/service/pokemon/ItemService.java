package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvItem;
import at.jku.faw.neo4jdemo.model.csv.CsvItemFlagMap;
import at.jku.faw.neo4jdemo.model.csv.CsvItemGameIndices;
import at.jku.faw.neo4jdemo.model.csv.CsvMachine;
import at.jku.faw.neo4jdemo.repository.csv.CsvItemFlagMapRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvItemGameIndicesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvItemRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvMachineRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.FlingEffectRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.GameIndexRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.ItemCategoryRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.ItemFlagRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.ItemRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.MachineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemService implements IPokemonDataLoader {

    private final CsvItemRepositoryImpl csvMainRepo;
    private final ItemRepository itemRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final FlingEffectRepository flingEffectRepository;
    private final CsvItemFlagMapRepositoryImpl csvItemFlagMapRepositoryImpl;
    private final ItemFlagRepository itemFlagRepository;
    private final CsvMachineRepositoryImpl csvMachineRepositoryImpl;
    private final MachineRepository machineRepository;
    private final CsvItemGameIndicesRepositoryImpl csvItemGameIndicesRepositoryImpl;
	private final GameIndexRepository gameIndexRepository;

	public ItemService(CsvItemRepositoryImpl csvMainRepo,
					   ItemRepository neo4jRepo, ItemCategoryRepository itemCategoryRepository,
					   FlingEffectRepository flingEffectRepository,
					   CsvItemFlagMapRepositoryImpl csvItemFlagMapRepositoryImpl, ItemFlagRepository itemFlagRepository,
					   CsvMachineRepositoryImpl csvMachineRepositoryImpl, MachineRepository machineRepository,
					   CsvItemGameIndicesRepositoryImpl csvItemGameIndicesRepositoryImpl,
					   GameIndexRepository gameIndexRepository) {
        this.csvMainRepo = csvMainRepo;
        this.itemRepository = neo4jRepo;
        this.itemCategoryRepository = itemCategoryRepository;
        this.flingEffectRepository = flingEffectRepository;
        this.csvItemFlagMapRepositoryImpl = csvItemFlagMapRepositoryImpl;
        this.itemFlagRepository = itemFlagRepository;
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
		for (CsvItem csv : csvMainRepo.getAll()) {
			itemRepository.insertItem(csv.getId(), csv.getIdentifier(), csv.getName(), csv.getCost(), csv.getFlingPower(),
					csv.getShortEffect(), csv.getEffect());
		}
	}

    @Override
    @Transactional
    public void loadRelationships() {
		for (CsvItem csvItem : csvMainRepo.getAll()) {
			if (csvItem.getCategoryId() != null) {
				itemCategoryRepository.findById(csvItem.getCategoryId()).ifPresent(itemCategory -> {
					itemRepository.linkItemToItemCategory(csvItem.getId(), itemCategory.getId());
				});
			}
			if (csvItem.getFlingEffectId() != null) {
				flingEffectRepository.findById(csvItem.getFlingEffectId()).ifPresentOrElse(
						(flingEffect) -> {
							itemRepository.linkItemToFlingEffect(csvItem.getId(), flingEffect.getId());
						},
						() -> {
							itemRepository.linkItemToFlingEffect(csvItem.getId(),
									csvItem.getFlingEffectId());
						});
			}
			for (CsvItemFlagMap csvItemFlagMap : csvItemFlagMapRepositoryImpl.getByItemId(csvItem.getId())) {
				itemFlagRepository.findById(csvItemFlagMap.getItemFlagId()).ifPresent(itemFlag -> {
					itemRepository.linkItemToItemFlag(csvItem.getId(), itemFlag.getId());
				});
			}

			for (CsvItemGameIndices csvItemGameIndices : csvItemGameIndicesRepositoryImpl.getByItemId(csvItem.getId())) {
				gameIndexRepository.linkItemHasGameIndex(csvItemGameIndices.getItemId(), csvItemGameIndices.getGenerationId(),
						csvItemGameIndices.getGameIndex());
			}

			for (CsvMachine csvMachine : csvMachineRepositoryImpl.getByItemId(csvItem.getId())) {
				machineRepository.findAll().forEach(machine ->
						itemRepository.linkItemToMachine(csvMachine.getItemId(), machine.getId()));
			}
		}
	}
}
