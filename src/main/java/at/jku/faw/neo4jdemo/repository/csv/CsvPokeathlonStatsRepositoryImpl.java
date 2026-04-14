package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvPokeathlonStats;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvPokeathlonStatsRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvPokeathlonStats> {

    @Value("classpath:data/pokeathlon_stats.csv")
    private Resource csvFile;
    
    private final List<CsvPokeathlonStats> csvPokeathlonStats = new ArrayList<>();

    @Override
    public CsvPokeathlonStats getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.getId(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvPokeathlonStats> getAll() {
        if (csvPokeathlonStats.isEmpty()) {
            try {
                csvPokeathlonStats.addAll(getCsvEntities(csvFile, CsvPokeathlonStats.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/pokeathlon_stats.csv", e);
            }
        }
        return csvPokeathlonStats;
    }
}
