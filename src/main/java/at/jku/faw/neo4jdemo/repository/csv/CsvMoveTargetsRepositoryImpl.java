package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvMoveTargets;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvMoveTargetsRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvMoveTargets> {

    @Value("classpath:data/move_targets.csv")
    private Resource csvFile;
    
    private final List<CsvMoveTargets> csvMoveTargets = new ArrayList<>();

    @Override
    public CsvMoveTargets getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.getId(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvMoveTargets> getAll() {
        if (csvMoveTargets.isEmpty()) {
            try {
                csvMoveTargets.addAll(getCsvEntities(csvFile, CsvMoveTargets.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/move_targets.csv", e);
            }
        }
        return csvMoveTargets;
    }
}
