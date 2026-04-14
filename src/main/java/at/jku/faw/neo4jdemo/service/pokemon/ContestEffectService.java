package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvContestEffectsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.ContestEffectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContestEffectService implements IPokemonDataLoader {

    private final CsvContestEffectsRepositoryImpl csvMainRepo;
    private final ContestEffectRepository neo4jRepo;

    public ContestEffectService(CsvContestEffectsRepositoryImpl csvMainRepo,
                           ContestEffectRepository neo4jRepo) {
        this.csvMainRepo = csvMainRepo;
        this.neo4jRepo = neo4jRepo;
    }

    @Override
    public String getEntityName() { return "ContestEffect"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvMainRepo.getAll().forEach(csv -> {
            neo4jRepo.insertContestEffect(csv.getId(), csv.getAppeal(), csv.getJam(), csv.getFlavorText(), csv.getEffect());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
