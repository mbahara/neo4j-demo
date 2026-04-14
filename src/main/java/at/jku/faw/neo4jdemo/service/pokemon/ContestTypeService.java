package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvContestTypesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.ContestTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContestTypeService implements IPokemonDataLoader {

    private final CsvContestTypesRepositoryImpl csvContestTypesRepository;
    private final ContestTypeRepository contestTypeRepository;


    public ContestTypeService(CsvContestTypesRepositoryImpl csvContestTypesRepository,
                           ContestTypeRepository contestTypeRepository) {
        this.csvContestTypesRepository = csvContestTypesRepository;
        this.contestTypeRepository = contestTypeRepository;

    }

    @Override
    public String getEntityName() { return "ContestType"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvContestTypesRepository.getAll().forEach(csv -> {
            contestTypeRepository.insertContestType(csv.id(), csv.identifier());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
