package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonEvolutionRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.EvolutionStepRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        List<Map<String, Object>> rows = csvMainRepo.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("minimumLevel", csv.getMinimumLevel());
                    row.put("minimumHappiness", csv.getMinimumHappiness());
                    row.put("timeOfDay", csv.getTimeOfDay());
                    row.put("relativePhysicalStats", csv.getRelativePhysicalStats());
                    row.put("needsOverworldRain", csv.getNeedsOverworldRain() != 0);
                    row.put("turnUpsideDown", csv.getTurnUpsideDown());
                    row.put("minimumBeauty", csv.getMinimumBeauty());
                    row.put("minimumAffection", csv.getMinimumAffection());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            neo4jRepo.batchInsertEvolutionSteps(rows);
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvMainRepo.getAll().forEach(csv -> {
            if (csv.getEvolutionTriggerId() != null) {
                neo4jRepo.linkEvolutionStepToEvolutionTrigger(csv.getId(), csv.getEvolutionTriggerId());
            }
            if (csv.getTriggerItemId() != null) {
                neo4jRepo.linkEvolutionStepToTriggerItem(csv.getId(), csv.getTriggerItemId());
            }
            if (csv.getGenderId() != null) {
                neo4jRepo.linkEvolutionStepToGender(csv.getId(), csv.getGenderId());
            }
            if (csv.getHeldItemId() != null) {
                neo4jRepo.linkEvolutionStepToItem(csv.getId(), csv.getHeldItemId());
            }
            if (csv.getKnownMoveId() != null) {
                neo4jRepo.linkEvolutionStepToMove(csv.getId(), csv.getKnownMoveId());
            }
            if (csv.getLocationId() != null) {
                neo4jRepo.linkEvolutionStepToLocation(csv.getId(), csv.getLocationId());
            }
            if (csv.getEvolvedSpeciesId() != null) {
                neo4jRepo.linkEvolutionStepToPokemonSpecies(csv.getId(), csv.getEvolvedSpeciesId());
            }
        });
    }
}
