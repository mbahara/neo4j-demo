package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvStats;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvStatsRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvStats> {

    @Value("classpath:data/stats.csv")
    private Resource csvFile;
    
    private final List<CsvStats> csvStats = new ArrayList<>();

    @Override
    public CsvStats getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.id(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvStats> getAll() {
        if (csvStats.isEmpty()) {
            try {
                csvStats.addAll(getCsvEntities(csvFile, CsvStats.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/stats.csv", e);
            }
        }
        return csvStats;
    }

    public List<CsvStats> getByDamageClassId(Long damageClassId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.damageClassId(), damageClassId))
                .toList();
    }
}
