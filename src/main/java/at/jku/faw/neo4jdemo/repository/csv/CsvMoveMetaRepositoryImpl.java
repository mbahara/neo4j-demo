package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvMoveMeta;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvMoveMetaRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvMoveMeta> {

    @Value("classpath:data/move_meta.csv")
    private Resource csvFile;
    
    private final List<CsvMoveMeta> csvMoveMeta = new ArrayList<>();

    @Override
    public CsvMoveMeta getById(Long id) {
        throw new UnsupportedOperationException("csvmovemeta.csv has no single ID.");
    }

    @Override
    public List<CsvMoveMeta> getAll() {
        if (csvMoveMeta.isEmpty()) {
            try {
                csvMoveMeta.addAll(getCsvEntities(csvFile, CsvMoveMeta.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/move_meta.csv", e);
            }
        }
        return csvMoveMeta;
    }

    public List<CsvMoveMeta> getByMoveId(Long moveId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.moveId(), moveId))
                .toList();
    }

    public List<CsvMoveMeta> getByMetaCategoryId(Long metaCategoryId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.metaCategoryId(), metaCategoryId))
                .toList();
    }

    public List<CsvMoveMeta> getByMetaAilmentId(Long metaAilmentId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.metaAilmentId(), metaAilmentId))
                .toList();
    }
}
