package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvEncounters;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvEncountersRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvEncounters> {

    @Value("classpath:data/encounters.csv")
    private Resource csvFile;
    
    private final List<CsvEncounters> csvEncounters = new ArrayList<>();

    @Override
    public CsvEncounters getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.getId(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvEncounters> getAll() {
        if (csvEncounters.isEmpty()) {
            try {
                csvEncounters.addAll(getCsvEntities(csvFile, CsvEncounters.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/encounters.csv", e);
            }
        }
        return csvEncounters;
    }

    public List<CsvEncounters> getByVersionId(Long versionId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getVersionId(), versionId))
                .toList();
    }

    public List<CsvEncounters> getByLocationAreaId(Long locationAreaId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getLocationAreaId(), locationAreaId))
                .toList();
    }

    public List<CsvEncounters> getByPokemonId(Long pokemonId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getPokemonId(), pokemonId))
                .toList();
    }
}
