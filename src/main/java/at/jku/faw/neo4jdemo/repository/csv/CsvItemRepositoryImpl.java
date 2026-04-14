package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvItem;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvItemRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvItem> {

    @Value("classpath:data/items.csv")
    private Resource csvFile;
    
    private final List<CsvItem> csvItem = new ArrayList<>();

    @Override
    public CsvItem getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.getId(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvItem> getAll() {
        if (csvItem.isEmpty()) {
            try {
                csvItem.addAll(getCsvEntities(csvFile, CsvItem.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/items.csv", e);
            }
        }
        return csvItem;
    }

    public List<CsvItem> getByCategoryId(Long categoryId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getCategoryId(), categoryId))
                .toList();
    }

    public List<CsvItem> getByFlingEffectId(Long flingEffectId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getFlingEffectId(), flingEffectId))
                .toList();
    }
}
