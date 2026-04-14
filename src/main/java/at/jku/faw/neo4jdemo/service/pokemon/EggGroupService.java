package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvEggGroupsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.EggGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EggGroupService implements IPokemonDataLoader {

    private final CsvEggGroupsRepositoryImpl csvMainRepo;
    private final EggGroupRepository neo4jRepo;


    public EggGroupService(CsvEggGroupsRepositoryImpl csvMainRepo,
                           EggGroupRepository neo4jRepo) {
        this.csvMainRepo = csvMainRepo;
        this.neo4jRepo = neo4jRepo;

    }

    @Override
    public String getEntityName() { return "EggGroup"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvMainRepo.getAll().forEach(csv -> {
            neo4jRepo.insertEggGroup(csv.getId(), csv.getIdentifier(), csv.getName());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
