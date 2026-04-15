package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvMoveMetaCategoriesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.MoveCategoryRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        List<Map<String, Object>> rows = csvMoveMetaCategoriesRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("identifier", csv.getIdentifier());
                    row.put("description", csv.getDescription());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            Integer count = moveCategoryRepository.batchInsertMoveCategories(rows);
            System.out.println("Successfully loaded " + count + " MoveCategories nodes.");
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
