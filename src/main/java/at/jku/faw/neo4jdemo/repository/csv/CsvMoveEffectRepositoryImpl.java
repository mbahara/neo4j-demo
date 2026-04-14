package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvMoveEffect;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvMoveEffectRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvMoveEffect> {

    @Value("classpath:data/move_effect.csv")
    private Resource csvFile;
    
    private final List<CsvMoveEffect> csvMoveEffect = new ArrayList<>();

    @Override
    public CsvMoveEffect getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.getId(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvMoveEffect> getAll() {
        if (csvMoveEffect.isEmpty()) {
            try {
                csvMoveEffect.addAll(getCsvEntities(csvFile, CsvMoveEffect.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/move_effect.csv", e);
            }
        }
        return csvMoveEffect;
    }
}
