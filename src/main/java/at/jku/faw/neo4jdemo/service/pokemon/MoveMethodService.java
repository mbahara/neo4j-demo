package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonMoveMethodsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.MoveMethodRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        List<Map<String, Object>> rows = csvPokemonMoveMethodsRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("identifier", csv.getIdentifier());
                    row.put("name", csv.getName());
                    row.put("description", csv.getDescription());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            moveMethodRepository.batchInsertMoveMethods(rows);
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
