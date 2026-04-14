package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvNature;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvNatureRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvNature> {

    @Value("classpath:data/natures.csv")
    private Resource csvFile;
    
    private final List<CsvNature> csvNature = new ArrayList<>();

    @Override
    public CsvNature getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.getId(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvNature> getAll() {
        if (csvNature.isEmpty()) {
            try {
                csvNature.addAll(getCsvEntities(csvFile, CsvNature.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/natures.csv", e);
            }
        }
        return csvNature;
    }

    public List<CsvNature> getByDecreasedStatId(Long decreasedStatId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getDecreasedStatId(), decreasedStatId))
                .toList();
    }

    public List<CsvNature> getByIncreasedStatId(Long increasedStatId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getIncreasedStatId(), increasedStatId))
                .toList();
    }
}
