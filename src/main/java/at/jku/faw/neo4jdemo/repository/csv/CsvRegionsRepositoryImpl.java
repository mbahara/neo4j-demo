package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvRegions;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvRegionsRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvRegions> {

    @Value("classpath:data/regions.csv")
    private Resource csvFile;
    
    private final List<CsvRegions> csvRegions = new ArrayList<>();

    @Override
    public CsvRegions getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.getId(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvRegions> getAll() {
        if (csvRegions.isEmpty()) {
            try {
                csvRegions.addAll(getCsvEntities(csvFile, CsvRegions.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/regions.csv", e);
            }
        }
        return csvRegions;
    }
}
