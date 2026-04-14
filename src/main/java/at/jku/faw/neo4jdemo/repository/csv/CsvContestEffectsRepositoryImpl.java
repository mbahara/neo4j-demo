package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvContestEffects;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvContestEffectsRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvContestEffects> {

    @Value("classpath:data/contest_effects.csv")
    private Resource csvFile;
    
    private final List<CsvContestEffects> csvContestEffects = new ArrayList<>();

    @Override
    public CsvContestEffects getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.getId(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvContestEffects> getAll() {
        if (csvContestEffects.isEmpty()) {
            try {
                csvContestEffects.addAll(getCsvEntities(csvFile, CsvContestEffects.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/contest_effects.csv", e);
            }
        }
        return csvContestEffects;
    }
}
