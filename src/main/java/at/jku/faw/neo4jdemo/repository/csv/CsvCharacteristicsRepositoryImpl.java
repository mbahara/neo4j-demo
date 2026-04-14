package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvCharacteristics;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvCharacteristicsRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvCharacteristics> {

    @Value("classpath:data/characteristics.csv")
    private Resource csvFile;
    
    private final List<CsvCharacteristics> csvCharacteristics = new ArrayList<>();

    @Override
    public CsvCharacteristics getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.getId(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvCharacteristics> getAll() {
        if (csvCharacteristics.isEmpty()) {
            try {
                csvCharacteristics.addAll(getCsvEntities(csvFile, CsvCharacteristics.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/characteristics.csv", e);
            }
        }
        return csvCharacteristics;
    }

    public List<CsvCharacteristics> getByStatId(Long statId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getStatId(), statId))
                .toList();
    }
}
