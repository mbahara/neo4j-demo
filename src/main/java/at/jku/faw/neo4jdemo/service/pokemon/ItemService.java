package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvItemFlagMapRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvItemGameIndicesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvItemRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvMachineRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.FlingEffectRepository;
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

    public ItemService(CsvItemRepositoryImpl csvMainRepo,
                       ItemRepository neo4jRepo, ItemCategoryRepository itemCategoryRepository,
                       FlingEffectRepository flingEffectRepository,
                       CsvItemFlagMapRepositoryImpl csvItemFlagMapRepositoryImpl, ItemFlagRepository itemFlagRepository,
                       CsvMachineRepositoryImpl csvMachineRepositoryImpl, MachineRepository machineRepository,
                       CsvItemGameIndicesRepositoryImpl csvItemGameIndicesRepositoryImpl) {
        this.csvMainRepo = csvMainRepo;
        this.itemRepository = neo4jRepo;
        this.itemCategoryRepository = itemCategoryRepository;
        this.flingEffectRepository = flingEffectRepository;
        this.csvItemFlagMapRepositoryImpl = csvItemFlagMapRepositoryImpl;
        this.itemFlagRepository = itemFlagRepository;
        this.csvMachineRepositoryImpl = csvMachineRepositoryImpl;
        this.machineRepository = machineRepository;
        this.csvItemGameIndicesRepositoryImpl = csvItemGameIndicesRepositoryImpl;
    }

    @Override
    public String getEntityName() { return "Item"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvMainRepo.getAll().forEach(csv -> {
            itemRepository.insertItem(csv.id(), csv.identifier(), csv.name(), csv.cost(), csv.flingPower(), csv.shortEffect(), csv.effect());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvMainRepo.getAll().forEach(csvItem -> {
            if (csvItem.categoryId() != null) {
                itemCategoryRepository.findById(csvItem.categoryId()).ifPresent(itemCategory -> {
                    itemRepository.linkItemToItemCategory(csvItem.id(), itemCategory.getId());
                });
            }
            if (csvItem.flingEffectId() != null) {
                flingEffectRepository.findById(csvItem.flingEffectId()).ifPresentOrElse(
                        (flingEffect) -> {
                            itemRepository.linkItemToFlingEffect(csvItem.id(), flingEffect.getId());
                        },
                        () -> {
                            itemRepository.linkItemToFlingEffect(csvItem.id(),
                                    csvItem.flingEffectId());
                        });
            }
            csvItemFlagMapRepositoryImpl.getByItemId(csvItem.id()).forEach(csvItemFlagMap -> {
                itemFlagRepository.findById(csvItemFlagMap.itemFlagId()).ifPresent(itemFlag -> {
                    itemRepository.linkItemToItemFlag(csvItem.id(), itemFlag.getId());
                });
            });

            csvItemGameIndicesRepositoryImpl.getByItemId(csvItem.id()).forEach(csvItemGameIndices -> {
               itemRepository.linkItemHasGameIndex(csvItemGameIndices.itemId(), csvItemGameIndices.generationId(), csvItemGameIndices.gameIndex());
            });

            csvMachineRepositoryImpl.getByItemId(csvItem.id()).forEach(csvMachine -> {
                machineRepository.findAll().forEach(machine ->
                                    itemRepository.linkItemToMachine(csvMachine.itemId(), machine.getId()));
            });
        });
    }
}
