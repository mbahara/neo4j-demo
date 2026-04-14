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
            neo4jRepo.insertEvolutionStep(csv.id(), csv.minimumLevel(), csv.minimumHappiness(), csv.timeOfDay(), csv.relativePhysicalStats(), csv.needsOverworldRain() != 0, csv.turnUpsideDown(), csv.minimumBeauty(), csv.minimumAffection());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvMainRepo.getAll().forEach(csv -> {
            if (csv.evolutionTriggerId() != null) {
                neo4jRepo.linkEvolutionStepToEvolutionTrigger(csv.id(), csv.evolutionTriggerId());
            }
        });
        csvMainRepo.getAll().forEach(csv -> {
            if (csv.triggerItemId() != null) {
                neo4jRepo.linkEvolutionStepToTriggerItem(csv.id(), csv.triggerItemId());
            }
        });
        csvMainRepo.getAll().forEach(csv -> {
            if (csv.genderId() != null) {
                neo4jRepo.linkEvolutionStepToGender(csv.id(), csv.genderId());
            }
        });
        csvMainRepo.getAll().forEach(csv -> {
            if (csv.heldItemId() != null) {
                neo4jRepo.linkEvolutionStepToItem(csv.id(), csv.heldItemId());
            }
        });
        csvMainRepo.getAll().forEach(csv -> {
            if (csv.knownMoveId() != null) {
                neo4jRepo.linkEvolutionStepToMove(csv.id(), csv.knownMoveId());
            }
        });
        csvMainRepo.getAll().forEach(csv -> {
            if (csv.locationId() != null) {
                neo4jRepo.linkEvolutionStepToLocation(csv.id(), csv.locationId());
            }
        });
        csvMainRepo.getAll().forEach(csv -> {
            if (csv.evolvedSpeciesId() != null) {
                neo4jRepo.linkEvolutionStepToPokemonSpecies(csv.id(), csv.evolvedSpeciesId());
            }
        });
    }
}
