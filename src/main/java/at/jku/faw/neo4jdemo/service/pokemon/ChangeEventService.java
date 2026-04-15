package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvAbilityChangelogRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.ChangeEventRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        List<Map<String, Object>> rows = csvMainRepo.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("effect", csv.getEffect());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            Integer count = neo4jRepo.batchInsertChangeEvents(rows);
            System.out.println("Successfully loaded " + count + " ChangeEvents nodes.");
        }
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
