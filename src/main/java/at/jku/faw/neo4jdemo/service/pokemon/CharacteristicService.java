package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.neo4j.Highlights;
import at.jku.faw.neo4jdemo.repository.csv.CsvCharacteristicsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.CharacteristicRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.StatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CharacteristicService implements IPokemonDataLoader {

    private final CsvCharacteristicsRepositoryImpl csvMainRepo;
    private final CharacteristicRepository neo4jRepo;
    private final StatsRepository statsRepository;

    public CharacteristicService(CsvCharacteristicsRepositoryImpl csvMainRepo,
                                 CharacteristicRepository neo4jRepo, StatsRepository statsRepository) {
        this.csvMainRepo = csvMainRepo;
        this.neo4jRepo = neo4jRepo;
        this.statsRepository = statsRepository;
    }

    @Override
    public String getEntityName() { return "Characteristic"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvMainRepo.getAll().forEach(csv -> {
            neo4jRepo.insertCharacteristic(csv.id(), csv.description());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvMainRepo.getAll().forEach(csvCharacteristics -> {
            Highlights highlights = new Highlights();
            highlights.setGeneMod5(csvCharacteristics.geneMod5());
            statsRepository.findById(csvCharacteristics.statId()).ifPresent(stats -> {
                highlights.setStat(stats);
                neo4jRepo.linkCharacteristicToStat(csvCharacteristics.id(), stats.getId(), highlights.getGeneMod5());
            });
        });
    }
}
