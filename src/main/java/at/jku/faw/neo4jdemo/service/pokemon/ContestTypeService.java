package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvContestTypesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.ContestTypeRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        List<Map<String, Object>> rows = csvContestTypesRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("identifier", csv.getIdentifier());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            contestTypeRepository.batchInsertContestTypes(rows);
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
