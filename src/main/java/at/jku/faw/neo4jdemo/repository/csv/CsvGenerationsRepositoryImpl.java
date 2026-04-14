package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvGenerations;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvGenerationsRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvGenerations> {

    @Value("classpath:data/generations.csv")
    private Resource csvFile;
    
    private final List<CsvGenerations> csvGenerations = new ArrayList<>();

    @Override
    public CsvGenerations getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.getId(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvGenerations> getAll() {
        if (csvGenerations.isEmpty()) {
            try {
                csvGenerations.addAll(getCsvEntities(csvFile, CsvGenerations.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/generations.csv", e);
            }
        }
        return csvGenerations;
    }

    public List<CsvGenerations> getByMainRegionId(Long mainRegionId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getMainRegionId(), mainRegionId))
                .toList();
    }
}
