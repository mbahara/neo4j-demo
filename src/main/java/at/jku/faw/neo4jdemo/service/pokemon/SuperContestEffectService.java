package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvSuperContestEffectsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.SuperContestEffectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SuperContestEffectService implements IPokemonDataLoader {

    private final CsvSuperContestEffectsRepositoryImpl csvSuperContestEffectsRepository;
    private final SuperContestEffectRepository superContestEffectRepository;

    public SuperContestEffectService(CsvSuperContestEffectsRepositoryImpl csvSuperContestEffectsRepository,
                                     SuperContestEffectRepository superContestEffectRepository) {
        this.csvSuperContestEffectsRepository = csvSuperContestEffectsRepository;
        this.superContestEffectRepository = superContestEffectRepository;
    }

    @Override
    public String getEntityName() { return "SuperContestEffect"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvSuperContestEffectsRepository.getAll().forEach(csv -> {
            superContestEffectRepository.insertSuperContestEffect(csv.id(), csv.appeal(), csv.flavorText());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        //
    }
}
