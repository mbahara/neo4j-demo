package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonMoveMethodsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.MoveMethodRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MoveMethodService implements IPokemonDataLoader {

    private final CsvPokemonMoveMethodsRepositoryImpl csvPokemonMoveMethodsRepository;
    private final MoveMethodRepository moveMethodRepository;

    public MoveMethodService(CsvPokemonMoveMethodsRepositoryImpl csvPokemonMoveMethodsRepository,
                           MoveMethodRepository moveMethodRepository) {
        this.csvPokemonMoveMethodsRepository = csvPokemonMoveMethodsRepository;
        this.moveMethodRepository = moveMethodRepository;
    }

    @Override
    public String getEntityName() { return "MoveMethod"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvPokemonMoveMethodsRepository.getAll().forEach(csv -> {
            moveMethodRepository.insertMoveMethod(csv.id(), csv.identifier(), csv.name(), csv.description());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
