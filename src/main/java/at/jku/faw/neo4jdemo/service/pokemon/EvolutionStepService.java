package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonEvolutionRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.EvolutionStepRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EvolutionStepService implements IPokemonDataLoader {

    private final CsvPokemonEvolutionRepositoryImpl csvMainRepo;
    private final EvolutionStepRepository neo4jRepo;

    public EvolutionStepService(CsvPokemonEvolutionRepositoryImpl csvMainRepo,
                                EvolutionStepRepository neo4jRepo) {
        this.csvMainRepo = csvMainRepo;
        this.neo4jRepo = neo4jRepo;
    }

    @Override
    public String getEntityName() { return "EvolutionStep"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvMainRepo.getAll().forEach(csv -> {
            neo4jRepo.insertEvolutionStep(csv.getId(), csv.getMinimumLevel(), csv.getMinimumHappiness(), csv.getTimeOfDay(), csv.getRelativePhysicalStats(), csv.getNeedsOverworldRain() != 0, csv.getTurnUpsideDown(), csv.getMinimumBeauty(), csv.getMinimumAffection());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvMainRepo.getAll().forEach(csv -> {
            if (csv.getEvolutionTriggerId() != null) {
                neo4jRepo.linkEvolutionStepToEvolutionTrigger(csv.getId(), csv.getEvolutionTriggerId());
            }
        });
        csvMainRepo.getAll().forEach(csv -> {
            if (csv.getTriggerItemId() != null) {
                neo4jRepo.linkEvolutionStepToTriggerItem(csv.getId(), csv.getTriggerItemId());
            }
        });
        csvMainRepo.getAll().forEach(csv -> {
            if (csv.getGenderId() != null) {
                neo4jRepo.linkEvolutionStepToGender(csv.getId(), csv.getGenderId());
            }
        });
        csvMainRepo.getAll().forEach(csv -> {
            if (csv.getHeldItemId() != null) {
                neo4jRepo.linkEvolutionStepToItem(csv.getId(), csv.getHeldItemId());
            }
        });
        csvMainRepo.getAll().forEach(csv -> {
            if (csv.getKnownMoveId() != null) {
                neo4jRepo.linkEvolutionStepToMove(csv.getId(), csv.getKnownMoveId());
            }
        });
        csvMainRepo.getAll().forEach(csv -> {
            if (csv.getLocationId() != null) {
                neo4jRepo.linkEvolutionStepToLocation(csv.getId(), csv.getLocationId());
            }
        });
        csvMainRepo.getAll().forEach(csv -> {
            if (csv.getEvolvedSpeciesId() != null) {
                neo4jRepo.linkEvolutionStepToPokemonSpecies(csv.getId(), csv.getEvolvedSpeciesId());
            }
        });
    }
}
