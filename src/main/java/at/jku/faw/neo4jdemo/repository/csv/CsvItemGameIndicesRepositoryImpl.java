package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvItemGameIndices;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvItemGameIndicesRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvItemGameIndices> {

    @Value("classpath:data/item_game_indices.csv")
    private Resource csvFile;
    
    private final List<CsvItemGameIndices> csvItemGameIndices = new ArrayList<>();

    @Override
    public CsvItemGameIndices getById(Long id) {
        throw new UnsupportedOperationException("csvitemgameindices.csv has no single ID.");
    }

    @Override
    public List<CsvItemGameIndices> getAll() {
        if (csvItemGameIndices.isEmpty()) {
            try {
                csvItemGameIndices.addAll(getCsvEntities(csvFile, CsvItemGameIndices.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/item_game_indices.csv", e);
            }
        }
        return csvItemGameIndices;
    }

    public List<CsvItemGameIndices> getByItemId(Long itemId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getItemId(), itemId))
                .toList();
    }

    public List<CsvItemGameIndices> getByGenerationId(Long generationId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getGenerationId(), generationId))
                .toList();
    }
}
