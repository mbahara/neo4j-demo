package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvAbilityChangelogRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.ChangeEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChangeEventService implements IPokemonDataLoader {

    private final CsvAbilityChangelogRepositoryImpl csvMainRepo;
    private final ChangeEventRepository neo4jRepo;

    public ChangeEventService(CsvAbilityChangelogRepositoryImpl csvMainRepo,
                           ChangeEventRepository neo4jRepo) {
        this.csvMainRepo = csvMainRepo;
        this.neo4jRepo = neo4jRepo;
    }

    @Override
    public String getEntityName() { return "ChangeEvent"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvMainRepo.getAll().forEach(csv -> {
            neo4jRepo.insertChangeEvent(csv.getId(), csv.getEffect());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvMainRepo.getAll().forEach(csv -> {
            if (csv.getVersionGroupId() != null) {
                neo4jRepo.linkChangeEventToVersionGroup(csv.getId(), csv.getVersionGroupId());
            }
        });
    }
}
