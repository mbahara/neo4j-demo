package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvMoveMeta;
import at.jku.faw.neo4jdemo.repository.csv.CsvMoveMetaRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.MoveMetaRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MoveMetaService implements IPokemonDataLoader {

    private final CsvMoveMetaRepositoryImpl csvMoveMetaRepository;
    private final MoveMetaRepository moveMetaRepository;

    public MoveMetaService(CsvMoveMetaRepositoryImpl csvMoveMetaRepository,
                           MoveMetaRepository moveMetaRepository) {
        this.csvMoveMetaRepository = csvMoveMetaRepository;
        this.moveMetaRepository = moveMetaRepository;
    }

    @Override
    public String getEntityName() { return "MoveMeta"; }

    @Override
    @Transactional
    public void loadNodes() {
        List<CsvMoveMeta> allCsv = csvMoveMetaRepository.getAll();

        long idCounter = 1;
        List<Map<String, Object>> rows = new ArrayList<>();

        for (CsvMoveMeta csv : allCsv) {
            Map<String, Object> row = new HashMap<>();
            row.put("id", idCounter++);
            row.put("minHits", csv.getMinHits());
            row.put("maxHits", csv.getMaxHits());
            row.put("minTurns", csv.getMinTurns());
            row.put("maxTurns", csv.getMaxTurns());
            row.put("drain", csv.getDrain());
            row.put("healing", csv.getHealing());
            row.put("critRate", csv.getCritRate());
            row.put("ailmentChance", csv.getAilmentChance());
            row.put("flinchChance", csv.getFlinchChance());
            row.put("statChance", csv.getStatChance());
            rows.add(row);
        }

        if (!rows.isEmpty()) {
            Integer count = moveMetaRepository.batchInsertMoveMeta(rows);
            moveMetaRepository.createMoveMetaIdIndex();
            System.out.println("Successfully loaded " + count + " MoveMeta nodes.");
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        List<CsvMoveMeta> allCsv = csvMoveMetaRepository.getAll();
        long idCounter = 1;
        for (CsvMoveMeta csv : allCsv) {
            long currentMoveMetaId = idCounter++;
            if (csv.getMoveId() != null) {
                if (csv.getMetaAilmentId() != null) {
                    moveMetaRepository.linkMoveMetaToMoveAilment(currentMoveMetaId, csv.getMetaAilmentId());
                }
                if (csv.getMetaCategoryId() != null) {
                    moveMetaRepository.linkMoveMetaToMoveCategory(currentMoveMetaId, csv.getMetaCategoryId());
                }
            }
        }
    }
}
