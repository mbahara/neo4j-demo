package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvVersions;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvVersionsRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvVersions> {

    @Value("classpath:data/versions.csv")
    private Resource csvFile;
    
    private final List<CsvVersions> csvVersions = new ArrayList<>();

    @Override
    public CsvVersions getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.id(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvVersions> getAll() {
        if (csvVersions.isEmpty()) {
            try {
                csvVersions.addAll(getCsvEntities(csvFile, CsvVersions.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/versions.csv", e);
            }
        }
        return csvVersions;
    }

    public List<CsvVersions> getByVersionGroupId(Long versionGroupId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.versionGroupId(), versionGroupId))
                .toList();
    }
}
