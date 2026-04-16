package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvMoveChangelog;
import at.jku.faw.neo4jdemo.repository.csv.CsvMoveChangelogRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.MoveChangeRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MoveChangeService implements IPokemonDataLoader {

    private final CsvMoveChangelogRepositoryImpl csvMoveChangelogRepositoryImpl;
    private final MoveChangeRepository moveChangeRepository;

    public MoveChangeService(CsvMoveChangelogRepositoryImpl csvMoveChangelogRepositoryImpl,
                             MoveChangeRepository moveChangeRepository) {
        this.csvMoveChangelogRepositoryImpl = csvMoveChangelogRepositoryImpl;
        this.moveChangeRepository = moveChangeRepository;
    }

    @Override
    public String getEntityName() { return "MoveChange"; }

    @Override
    @Transactional
    public void loadNodes() {
        List<CsvMoveChangelog> allCsv = csvMoveChangelogRepositoryImpl.getAll();

        long idCounter = 1;
        List<Map<String, Object>> rows = new ArrayList<>();

        for (CsvMoveChangelog csv : allCsv) {
            Map<String, Object> row = new HashMap<>();
            row.put("id", idCounter++);
            row.put("power", csv.getPower());
            row.put("pp", csv.getPp());
            row.put("accuracy", csv.getAccuracy());
            row.put("priority", csv.getPriority());
            rows.add(row);
        }

        if (!rows.isEmpty()) {
            Integer count = moveChangeRepository.batchInsertMoveChanges(rows);
            moveChangeRepository.createChangeIdIndex();
            System.out.println("Successfully loaded " + count + " MoveChange nodes.");
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        List<CsvMoveChangelog> allCsv = csvMoveChangelogRepositoryImpl.getAll();

        long idCounter = 1;
        for (CsvMoveChangelog csv : allCsv) {
            long currentChangeId = idCounter++;

            if (csv.getVersionGroupId() != null) {
                moveChangeRepository.linkMoveChangeToVersionGroup(currentChangeId, csv.getVersionGroupId());
            }
            if (csv.getTypeId() != null) {
                moveChangeRepository.linkMoveChangeToType(currentChangeId, csv.getTypeId());
            }
            if (csv.getEffectId() != null) {
                moveChangeRepository.linkMoveChangeToMoveEffect(currentChangeId, csv.getEffectId(), csv.getEffectChance());
            }
            if (csv.getTargetId() != null) {
                moveChangeRepository.linkMoveChangeToMoveTarget(currentChangeId, csv.getTargetId());
            }
        }
    }
}
