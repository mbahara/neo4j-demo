package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvPokemonEvolution;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvPokemonEvolutionRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvPokemonEvolution> {

    @Value("classpath:data/pokemon_evolution.csv")
    private Resource csvFile;
    
    private final List<CsvPokemonEvolution> csvPokemonEvolution = new ArrayList<>();

    @Override
    public CsvPokemonEvolution getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.id(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvPokemonEvolution> getAll() {
        if (csvPokemonEvolution.isEmpty()) {
            try {
                csvPokemonEvolution.addAll(getCsvEntities(csvFile, CsvPokemonEvolution.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/pokemon_evolution.csv", e);
            }
        }
        return csvPokemonEvolution;
    }

    public List<CsvPokemonEvolution> getByEvolvedSpeciesId(Long evolvedSpeciesId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.evolvedSpeciesId(), evolvedSpeciesId))
                .toList();
    }

    public List<CsvPokemonEvolution> getByEvolutionTriggerId(Long evolutionTriggerId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.evolutionTriggerId(), evolutionTriggerId))
                .toList();
    }

    public List<CsvPokemonEvolution> getByTriggerItemId(Long triggerItemId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.triggerItemId(), triggerItemId))
                .toList();
    }

    public List<CsvPokemonEvolution> getByGenderId(Long genderId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.genderId(), genderId))
                .toList();
    }

    public List<CsvPokemonEvolution> getByLocationId(Long locationId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.locationId(), locationId))
                .toList();
    }

    public List<CsvPokemonEvolution> getByHeldItemId(Long heldItemId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.heldItemId(), heldItemId))
                .toList();
    }

    public List<CsvPokemonEvolution> getByKnownMoveId(Long knownMoveId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.knownMoveId(), knownMoveId))
                .toList();
    }

    public List<CsvPokemonEvolution> getByKnownMoveTypeId(Long knownMoveTypeId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.knownMoveTypeId(), knownMoveTypeId))
                .toList();
    }
}
