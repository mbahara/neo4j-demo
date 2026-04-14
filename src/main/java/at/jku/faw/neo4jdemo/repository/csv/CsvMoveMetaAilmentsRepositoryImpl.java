package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvMoveMetaAilments;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvMoveMetaAilmentsRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvMoveMetaAilments> {

    @Value("classpath:data/move_meta_ailments.csv")
    private Resource csvFile;
    
    private final List<CsvMoveMetaAilments> csvMoveMetaAilments = new ArrayList<>();

    @Override
    public CsvMoveMetaAilments getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.getId(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvMoveMetaAilments> getAll() {
        if (csvMoveMetaAilments.isEmpty()) {
            try {
                csvMoveMetaAilments.addAll(getCsvEntities(csvFile, CsvMoveMetaAilments.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/move_meta_ailments.csv", e);
            }
        }
        return csvMoveMetaAilments;
    }
}
