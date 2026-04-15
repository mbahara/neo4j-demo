package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.neo4j.Stats;
import at.jku.faw.neo4jdemo.repository.csv.CsvCharacteristicsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.CharacteristicRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.HighlightsRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.StatsRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CharacteristicService implements IPokemonDataLoader {

    private final CsvCharacteristicsRepositoryImpl csvCharacteristicsRepository;
    private final CharacteristicRepository characteristicRepository;
    private final StatsRepository statsRepository;
    private final HighlightsRepository highlightsRepository;

    public CharacteristicService(CsvCharacteristicsRepositoryImpl csvCharacteristicsRepository,
                                 CharacteristicRepository characteristicRepository, StatsRepository statsRepository,
                                 HighlightsRepository highlightsRepository) {
        this.csvCharacteristicsRepository = csvCharacteristicsRepository;
        this.characteristicRepository = characteristicRepository;
        this.statsRepository = statsRepository;
        this.highlightsRepository = highlightsRepository;
    }

    @Override
    public String getEntityName() { return "Characteristic"; }

    @Override
    @Transactional
    public void loadNodes() {
        List<Map<String, Object>> rows = csvCharacteristicsRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("description", csv.getDescription());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            Integer count = characteristicRepository.batchInsertCharacteristics(rows);
            System.out.println("Successfully loaded " + count + " Characteristics nodes.");
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        Map<Long, Stats> statsLookup = statsRepository.findAll().stream()
                .collect(Collectors.toMap(
                        Stats::getId,
                        s -> s,
                        (existing, replacement) -> existing
                ));

        csvCharacteristicsRepository.getAll().forEach(csv -> {
            Stats stat = statsLookup.get(csv.getStatId());

            if (stat != null) {
                highlightsRepository.linkCharacteristicToStat(
                        csv.getId(),
                        stat.getId(),
                        csv.getGeneMod5()
                );
            }
        });
    }
}
