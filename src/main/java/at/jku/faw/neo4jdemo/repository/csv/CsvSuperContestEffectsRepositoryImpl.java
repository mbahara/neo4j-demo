package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvSuperContestEffects;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvSuperContestEffectsRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvSuperContestEffects> {

    @Value("classpath:data/super_contest_effects.csv")
    private Resource csvFile;
    
    private final List<CsvSuperContestEffects> csvSuperContestEffects = new ArrayList<>();

    @Override
    public CsvSuperContestEffects getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.id(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvSuperContestEffects> getAll() {
        if (csvSuperContestEffects.isEmpty()) {
            try {
                csvSuperContestEffects.addAll(getCsvEntities(csvFile, CsvSuperContestEffects.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/super_contest_effects.csv", e);
            }
        }
        return csvSuperContestEffects;
    }
}
