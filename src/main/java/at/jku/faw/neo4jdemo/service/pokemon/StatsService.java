package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvStatsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.StatsRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StatsService implements IPokemonDataLoader {

    private final CsvStatsRepositoryImpl csvStatsRepository;
    private final StatsRepository statsRepository;

    public StatsService(CsvStatsRepositoryImpl csvStatsRepository,
                           StatsRepository statsRepository) {
        this.csvStatsRepository = csvStatsRepository;
        this.statsRepository = statsRepository;
    }

    @Override
    public String getEntityName() { return "Stats"; }

    @Override
    @Transactional
    public void loadNodes() {
        List<Map<String, Object>> rows = csvStatsRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("identifier", csv.getIdentifier());
                    row.put("isBattleOnly", csv.getIsBattleOnly() != 0);
                    row.put("gameIndex", csv.getGameIndex());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            statsRepository.batchInsertStats(rows);
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvStatsRepository.getAll().forEach(csv -> {
            if (csv.getDamageClassId() != null) {
                statsRepository.linkStatsToDamageClass(csv.getId(), csv.getDamageClassId());
            }
        });
    }
}
