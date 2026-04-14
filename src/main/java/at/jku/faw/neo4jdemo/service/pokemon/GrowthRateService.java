package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.neo4j.Level;
import at.jku.faw.neo4jdemo.repository.csv.CsvExperienceRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvGrowthRatesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.GrowthRateRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.LevelRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.LevelRequirementRepository;
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
        csvMainRepo.getAll().forEach(csv -> {
            neo4jRepo.insertGrowthRate(csv.id(), csv.identifier(), csv.formula());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvMainRepo.getAll().forEach(rates -> {
            csvExperienceRepositoryImpl.getByGrowthRateId(rates.id()).forEach(experience -> {
                Level level = levelRepository.insertLevel(experience.level());
                levelRequirementRepository.linkGrowthRateToLevel(experience.growthRateId(), level.getId(), experience.experience());
            });

        });
    }
}
