package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvEncounterMethodsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.EncounterMethodRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EncounterMethodService implements IPokemonDataLoader {

    private final CsvEncounterMethodsRepositoryImpl csvMainRepo;
    private final EncounterMethodRepository neo4jRepo;


    public EncounterMethodService(CsvEncounterMethodsRepositoryImpl csvMainRepo,
                           EncounterMethodRepository neo4jRepo) {
        this.csvMainRepo = csvMainRepo;
        this.neo4jRepo = neo4jRepo;

    }

    @Override
    public String getEntityName() { return "EncounterMethod"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvMainRepo.getAll().forEach(csv -> {
            neo4jRepo.insertEncounterMethod(csv.getId(), csv.getIdentifier(), csv.getName(), csv.getOrder());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
