package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvPalParkAreas;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvPalParkAreasRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvPalParkAreas> {

    @Value("classpath:data/pal_park_areas.csv")
    private Resource csvFile;
    
    private final List<CsvPalParkAreas> csvPalParkAreas = new ArrayList<>();

    @Override
    public CsvPalParkAreas getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.id(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvPalParkAreas> getAll() {
        if (csvPalParkAreas.isEmpty()) {
            try {
                csvPalParkAreas.addAll(getCsvEntities(csvFile, CsvPalParkAreas.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/pal_park_areas.csv", e);
            }
        }
        return csvPalParkAreas;
    }
}
