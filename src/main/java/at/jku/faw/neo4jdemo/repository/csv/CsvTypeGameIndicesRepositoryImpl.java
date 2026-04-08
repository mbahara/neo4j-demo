package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvTypeGameIndices;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvTypeGameIndicesRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvTypeGameIndices> {

    @Value("classpath:data/type_game_indices.csv")
    private Resource csvFile;
    
    private final List<CsvTypeGameIndices> csvTypeGameIndices = new ArrayList<>();

    @Override
    public CsvTypeGameIndices getById(Long id) {
        throw new UnsupportedOperationException("csvtypegameindices.csv has no single ID.");
    }

    @Override
    public List<CsvTypeGameIndices> getAll() {
        if (csvTypeGameIndices.isEmpty()) {
            try {
                csvTypeGameIndices.addAll(getCsvEntities(csvFile, CsvTypeGameIndices.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/type_game_indices.csv", e);
            }
        }
        return csvTypeGameIndices;
    }

    public List<CsvTypeGameIndices> getByTypeId(Long typeId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.typeId(), typeId))
                .toList();
    }

    public List<CsvTypeGameIndices> getByGenerationId(Long generationId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.generationId(), generationId))
                .toList();
    }
}
