package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvLocation;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvLocationRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvLocation> {

    @Value("classpath:data/locations.csv")
    private Resource csvFile;
    
    private final List<CsvLocation> csvLocation = new ArrayList<>();

    @Override
    public CsvLocation getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.id(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvLocation> getAll() {
        if (csvLocation.isEmpty()) {
            try {
                csvLocation.addAll(getCsvEntities(csvFile, CsvLocation.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/locations.csv", e);
            }
        }
        return csvLocation;
    }

    public List<CsvLocation> getByRegionId(Long regionId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.regionId(), regionId))
                .toList();
    }
}
