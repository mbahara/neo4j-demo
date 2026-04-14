package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvGendersRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.GenderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GenderService implements IPokemonDataLoader {

    private final CsvGendersRepositoryImpl csvMainRepo;
    private final GenderRepository neo4jRepo;

    public GenderService(CsvGendersRepositoryImpl csvMainRepo,
                           GenderRepository neo4jRepo) {
        this.csvMainRepo = csvMainRepo;
        this.neo4jRepo = neo4jRepo;
    }

    @Override
    public String getEntityName() { return "Gender"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvMainRepo.getAll().forEach(csv -> {
            neo4jRepo.insertGender(csv.id(), csv.identifier());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
