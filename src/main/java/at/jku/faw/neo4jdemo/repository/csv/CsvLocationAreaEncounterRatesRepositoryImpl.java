package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvLocationAreaEncounterRates;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvLocationAreaEncounterRatesRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvLocationAreaEncounterRates> {

    @Value("classpath:data/location_area_encounter_rates.csv")
    private Resource csvFile;
    
    private final List<CsvLocationAreaEncounterRates> csvLocationAreaEncounterRates = new ArrayList<>();

    @Override
    public CsvLocationAreaEncounterRates getById(Long id) {
        throw new UnsupportedOperationException("csvlocationareaencounterrates.csv has no single ID.");
    }

    @Override
    public List<CsvLocationAreaEncounterRates> getAll() {
        if (csvLocationAreaEncounterRates.isEmpty()) {
            try {
                csvLocationAreaEncounterRates.addAll(getCsvEntities(csvFile, CsvLocationAreaEncounterRates.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/location_area_encounter_rates.csv", e);
            }
        }
        return csvLocationAreaEncounterRates;
    }

    public List<CsvLocationAreaEncounterRates> getByLocationAreaId(Long locationAreaId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getLocationAreaId(), locationAreaId))
                .toList();
    }

    public List<CsvLocationAreaEncounterRates> getByEncounterMethodId(Long encounterMethodId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getEncounterMethodId(), encounterMethodId))
                .toList();
    }

    public List<CsvLocationAreaEncounterRates> getByVersionId(Long versionId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getVersionId(), versionId))
                .toList();
    }
}
