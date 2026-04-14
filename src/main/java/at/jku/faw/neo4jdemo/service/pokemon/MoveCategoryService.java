package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvMoveMetaCategoriesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.MoveCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MoveCategoryService implements IPokemonDataLoader {

    private final CsvMoveMetaCategoriesRepositoryImpl csvMoveMetaCategoriesRepository;
    private final MoveCategoryRepository moveCategoryRepository;


    public MoveCategoryService(CsvMoveMetaCategoriesRepositoryImpl csvMoveMetaCategoriesRepository,
                           MoveCategoryRepository neo4jRepo) {
        this.csvMoveMetaCategoriesRepository = csvMoveMetaCategoriesRepository;
        this.moveCategoryRepository = neo4jRepo;
    }

    @Override
    public String getEntityName() { return "MoveCategory"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvMoveMetaCategoriesRepository.getAll().forEach(csv -> {
            moveCategoryRepository.insertMoveCategory(csv.id(), csv.identifier(), csv.description());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
