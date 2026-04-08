package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvEncounterMethods;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvEncounterMethodsRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvEncounterMethods> {

    @Value("classpath:data/encounter_methods.csv")
    private Resource csvFile;
    
    private final List<CsvEncounterMethods> csvEncounterMethods = new ArrayList<>();

    @Override
    public CsvEncounterMethods getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.id(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvEncounterMethods> getAll() {
        if (csvEncounterMethods.isEmpty()) {
            try {
                csvEncounterMethods.addAll(getCsvEntities(csvFile, CsvEncounterMethods.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/encounter_methods.csv", e);
            }
        }
        return csvEncounterMethods;
    }
}
