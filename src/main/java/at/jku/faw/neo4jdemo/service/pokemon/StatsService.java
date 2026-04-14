package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvStatsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.StatsRepository;
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
        csvStatsRepository.getAll().forEach(csv -> {
            statsRepository.insertStats(csv.getId(), csv.getIdentifier(), csv.getIsBattleOnly() != 0, csv.getGameIndex());
        });
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
