package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvNaturePokeathlonStats;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvNaturePokeathlonStatsRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvNaturePokeathlonStats> {

    @Value("classpath:data/nature_pokeathlon_stats.csv")
    private Resource csvFile;
    
    private final List<CsvNaturePokeathlonStats> csvNaturePokeathlonStats = new ArrayList<>();

    @Override
    public CsvNaturePokeathlonStats getById(Long id) {
        throw new UnsupportedOperationException("csvnaturepokeathlonstats.csv has no single ID.");
    }

    @Override
    public List<CsvNaturePokeathlonStats> getAll() {
        if (csvNaturePokeathlonStats.isEmpty()) {
            try {
                csvNaturePokeathlonStats.addAll(getCsvEntities(csvFile, CsvNaturePokeathlonStats.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/nature_pokeathlon_stats.csv", e);
            }
        }
        return csvNaturePokeathlonStats;
    }

    public List<CsvNaturePokeathlonStats> getByNatureId(Long natureId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getNatureId(), natureId))
                .toList();
    }

    public List<CsvNaturePokeathlonStats> getByPokeathlonStatId(Long pokeathlonStatId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getPokeathlonStatId(), pokeathlonStatId))
                .toList();
    }
}
