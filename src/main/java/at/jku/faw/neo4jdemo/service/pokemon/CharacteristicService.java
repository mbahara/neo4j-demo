package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvCharacteristicsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.CharacteristicRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.HighlightsRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.StatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CharacteristicService implements IPokemonDataLoader {

    private final CsvCharacteristicsRepositoryImpl csvMainRepo;
    private final CharacteristicRepository neo4jRepo;
    private final StatsRepository statsRepository;
    private final HighlightsRepository highlightsRepository;

    public CharacteristicService(CsvCharacteristicsRepositoryImpl csvMainRepo,
                                 CharacteristicRepository neo4jRepo, StatsRepository statsRepository,
                                 HighlightsRepository highlightsRepository) {
        this.csvMainRepo = csvMainRepo;
        this.neo4jRepo = neo4jRepo;
        this.statsRepository = statsRepository;
        this.highlightsRepository = highlightsRepository;
    }

    @Override
    public String getEntityName() { return "Characteristic"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvMainRepo.getAll().forEach(csv -> {
            neo4jRepo.insertCharacteristic(csv.getId(), csv.getDescription());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvMainRepo.getAll().forEach(csvCharacteristics -> {
            statsRepository.findById(csvCharacteristics.getStatId()).ifPresent(stats -> {
                highlightsRepository.linkCharacteristicToStat(csvCharacteristics.getId(), stats.getId(), csvCharacteristics.getGeneMod5());
            });
        });
    }
}
