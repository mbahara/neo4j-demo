package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvLocationAreas;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvLocationAreasRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvLocationAreas> {

    @Value("classpath:data/location_areas.csv")
    private Resource csvFile;
    
    private final List<CsvLocationAreas> csvLocationAreas = new ArrayList<>();

    @Override
    public CsvLocationAreas getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.id(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvLocationAreas> getAll() {
        if (csvLocationAreas.isEmpty()) {
            try {
                csvLocationAreas.addAll(getCsvEntities(csvFile, CsvLocationAreas.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/location_areas.csv", e);
            }
        }
        return csvLocationAreas;
    }

    public List<CsvLocationAreas> getByLocationId(Long locationId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.locationId(), locationId))
                .toList();
    }
}
