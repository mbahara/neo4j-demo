package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvItemPockets;
import at.jku.faw.neo4jdemo.repository.csv.CsvItemCategoriesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvItemPocketsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.ItemCategoryRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        List<Map<String, Object>> rows = csvMainRepo.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("identifier", csv.getIdentifier());
                    row.put("name", csv.getName());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            Integer count = neo4jRepo.batchInsertItemCategories(rows);
            System.out.println("Successfully loaded " + count + " ItemCategories nodes.");
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        Map<Long, CsvItemPockets> pocketMap = csvItemPocketsRepositoryImpl.getAll().stream()
                .collect(Collectors.toMap(CsvItemPockets::getId, p -> p));

        csvMainRepo.getAll().forEach(csv -> {
            if (csv.getPocketId() != null && pocketMap.containsKey(csv.getPocketId())) {
                neo4jRepo.linkItemCategoryToItemPocket(csv.getId(), csv.getPocketId());
            }
        });
    }
}
