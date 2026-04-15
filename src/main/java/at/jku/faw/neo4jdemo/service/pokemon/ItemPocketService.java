package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvItemPocketsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.ItemPocketRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemPocketService implements IPokemonDataLoader {

    private final CsvItemPocketsRepositoryImpl csvItemPocketsRepository;
    private final ItemPocketRepository itemPocketRepository;

    public ItemPocketService(CsvItemPocketsRepositoryImpl csvItemPocketsRepository,
                           ItemPocketRepository itemPocketRepository) {
        this.csvItemPocketsRepository = csvItemPocketsRepository;
        this.itemPocketRepository = itemPocketRepository;
    }

    @Override
    public String getEntityName() { return "ItemPocket"; }

    @Override
    @Transactional
    public void loadNodes() {
        List<Map<String, Object>> rows = csvItemPocketsRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("identifier", csv.getIdentifier());
                    row.put("name", csv.getName());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            itemPocketRepository.batchInsertItemPockets(rows);
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
