package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvExperience;
import at.jku.faw.neo4jdemo.model.neo4j.Level;
import at.jku.faw.neo4jdemo.repository.csv.CsvExperienceRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvGrowthRatesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.GrowthRateRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.LevelRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.LevelRequirementRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GrowthRateService implements IPokemonDataLoader {

    private final CsvGrowthRatesRepositoryImpl csvMainRepo;
    private final GrowthRateRepository neo4jRepo;
    private final CsvExperienceRepositoryImpl csvExperienceRepositoryImpl;
    private final LevelRepository levelRepository;
    private final LevelRequirementRepository levelRequirementRepository;


    public GrowthRateService(CsvGrowthRatesRepositoryImpl csvMainRepo,
                             GrowthRateRepository neo4jRepo, CsvExperienceRepositoryImpl csvExperienceRepositoryImpl,
                             LevelRepository levelRepository, LevelRequirementRepository levelRequirementRepository) {
        this.csvMainRepo = csvMainRepo;
        this.neo4jRepo = neo4jRepo;
        this.csvExperienceRepositoryImpl = csvExperienceRepositoryImpl;
        this.levelRepository = levelRepository;
        this.levelRequirementRepository = levelRequirementRepository;
    }

    @Override
    public String getEntityName() { return "GrowthRate"; }

    @Override
    @Transactional
    public void loadNodes() {
        List<Map<String, Object>> rows = csvMainRepo.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("identifier", csv.getIdentifier());
                    row.put("formula", csv.getFormula());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            Integer count = neo4jRepo.batchInsertGrowthRates(rows);
            System.out.println("Successfully loaded " + count + " GrowthRates nodes.");
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        Map<Long, List<CsvExperience>> experienceMap = csvExperienceRepositoryImpl.getAll().stream()
                .collect(Collectors.groupingBy(CsvExperience::getGrowthRateId));

        csvMainRepo.getAll().forEach(rate -> {
            Long growthRateId = rate.getId();
            List<CsvExperience> experiences = experienceMap.get(growthRateId);

            if (experiences != null) {
                experiences.forEach(exp -> {
                    Level level = levelRepository.insertLevel(exp.getLevel());
                    levelRequirementRepository.linkGrowthRateToLevel(
                            growthRateId,
                            level.getId(),
                            exp.getExperience()
                    );
                });
            }
        });
    }
}
