package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvItemCategoriesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvItemPocketsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.ItemCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemCategoryService implements IPokemonDataLoader {

    private final CsvItemCategoriesRepositoryImpl csvMainRepo;
    private final ItemCategoryRepository neo4jRepo;
    private final CsvItemPocketsRepositoryImpl csvItemPocketsRepositoryImpl;


    public ItemCategoryService(CsvItemCategoriesRepositoryImpl csvMainRepo,
                               ItemCategoryRepository neo4jRepo,
                               CsvItemPocketsRepositoryImpl csvItemPocketsRepositoryImpl) {
        this.csvMainRepo = csvMainRepo;
        this.neo4jRepo = neo4jRepo;
        this.csvItemPocketsRepositoryImpl = csvItemPocketsRepositoryImpl;
    }

    @Override
    public String getEntityName() { return "ItemCategory"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvMainRepo.getAll().forEach(csv -> {
            neo4jRepo.insertItemCategory(csv.id(), csv.identifier(), csv.name());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvMainRepo.getAll().forEach(csv -> {
            csvItemPocketsRepositoryImpl.getAll().forEach(csvItemPocket -> {
                if (csvItemPocket.id().equals(csv.pocketId())) {
                    neo4jRepo.linkItemCategoryToItemPocket(csv.id(), csvItemPocket.id());
                }
            });
        });
    }
}
