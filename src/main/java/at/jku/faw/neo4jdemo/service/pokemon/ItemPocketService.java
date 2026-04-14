package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvItemPocketsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.ItemPocketRepository;
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
        csvItemPocketsRepository.getAll().forEach(csv -> {
            itemPocketRepository.insertItemPocket(csv.getId(), csv.getIdentifier(), csv.getName());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
