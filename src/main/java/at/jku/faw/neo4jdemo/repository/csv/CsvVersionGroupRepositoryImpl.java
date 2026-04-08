package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvVersionGroup;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvVersionGroupRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvVersionGroup> {

    @Value("classpath:data/version_groups.csv")
    private Resource csvFile;
    
    private final List<CsvVersionGroup> csvVersionGroup = new ArrayList<>();

    @Override
    public CsvVersionGroup getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.id(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvVersionGroup> getAll() {
        if (csvVersionGroup.isEmpty()) {
            try {
                csvVersionGroup.addAll(getCsvEntities(csvFile, CsvVersionGroup.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/version_groups.csv", e);
            }
        }
        return csvVersionGroup;
    }

    public List<CsvVersionGroup> getByGenerationId(Long generationId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.generationId(), generationId))
                .toList();
    }
}
