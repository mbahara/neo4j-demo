package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvItemFlagsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.ItemFlagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemFlagService implements IPokemonDataLoader {

    private final CsvItemFlagsRepositoryImpl csvMainRepo;
    private final ItemFlagRepository neo4jRepo;

    public ItemFlagService(CsvItemFlagsRepositoryImpl csvMainRepo,
                           ItemFlagRepository neo4jRepo) {
        this.csvMainRepo = csvMainRepo;
        this.neo4jRepo = neo4jRepo;
    }

    @Override
    public String getEntityName() { return "ItemFlag"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvMainRepo.getAll().forEach(csv -> {
            neo4jRepo.insertItemFlag(csv.getId(), csv.getIdentifier(), csv.getName(), csv.getDescription());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
