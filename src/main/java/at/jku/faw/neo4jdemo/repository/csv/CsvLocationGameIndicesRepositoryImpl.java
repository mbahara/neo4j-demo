package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvLocationGameIndices;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvLocationGameIndicesRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvLocationGameIndices> {

    @Value("classpath:data/location_game_indices.csv")
    private Resource csvFile;
    
    private final List<CsvLocationGameIndices> csvLocationGameIndices = new ArrayList<>();

    @Override
    public CsvLocationGameIndices getById(Long id) {
        throw new UnsupportedOperationException("csvlocationgameindices.csv has no single ID.");
    }

    @Override
    public List<CsvLocationGameIndices> getAll() {
        if (csvLocationGameIndices.isEmpty()) {
            try {
                csvLocationGameIndices.addAll(getCsvEntities(csvFile, CsvLocationGameIndices.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/location_game_indices.csv", e);
            }
        }
        return csvLocationGameIndices;
    }

    public List<CsvLocationGameIndices> getByLocationId(Long locationId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getLocationId(), locationId))
                .toList();
    }

    public List<CsvLocationGameIndices> getByGenerationId(Long generationId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getGenerationId(), generationId))
                .toList();
    }
}
