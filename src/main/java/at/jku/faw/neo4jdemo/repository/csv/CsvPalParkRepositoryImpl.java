package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvPalPark;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvPalParkRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvPalPark> {

    @Value("classpath:data/pal_park.csv")
    private Resource csvFile;
    
    private final List<CsvPalPark> csvPalPark = new ArrayList<>();

    @Override
    public CsvPalPark getById(Long id) {
        throw new UnsupportedOperationException("csvpalpark.csv has no single ID.");
    }

    @Override
    public List<CsvPalPark> getAll() {
        if (csvPalPark.isEmpty()) {
            try {
                csvPalPark.addAll(getCsvEntities(csvFile, CsvPalPark.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/pal_park.csv", e);
            }
        }
        return csvPalPark;
    }

    public List<CsvPalPark> getBySpeciesId(Long speciesId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getSpeciesId(), speciesId))
                .toList();
    }

    public List<CsvPalPark> getByAreaId(Long areaId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getAreaId(), areaId))
                .toList();
    }
}
