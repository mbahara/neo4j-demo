package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvMoveDamageClassesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.DamageClassRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DamageClassService implements IPokemonDataLoader {

    private final CsvMoveDamageClassesRepositoryImpl csvMainRepo;
    private final DamageClassRepository neo4jRepo;

    public DamageClassService(CsvMoveDamageClassesRepositoryImpl csvMainRepo,
                           DamageClassRepository neo4jRepo) {
        this.csvMainRepo = csvMainRepo;
        this.neo4jRepo = neo4jRepo;
    }

    @Override
    public String getEntityName() { return "DamageClass"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvMainRepo.getAll().forEach(csv -> {
            neo4jRepo.insertDamageClass(csv.id(), csv.identifier(), csv.name(), csv.description());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
