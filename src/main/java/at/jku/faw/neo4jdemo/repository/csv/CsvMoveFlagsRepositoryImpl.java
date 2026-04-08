package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvMoveFlags;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvMoveFlagsRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvMoveFlags> {

    @Value("classpath:data/move_flags.csv")
    private Resource csvFile;
    
    private final List<CsvMoveFlags> csvMoveFlags = new ArrayList<>();

    @Override
    public CsvMoveFlags getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.id(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvMoveFlags> getAll() {
        if (csvMoveFlags.isEmpty()) {
            try {
                csvMoveFlags.addAll(getCsvEntities(csvFile, CsvMoveFlags.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/move_flags.csv", e);
            }
        }
        return csvMoveFlags;
    }
}
