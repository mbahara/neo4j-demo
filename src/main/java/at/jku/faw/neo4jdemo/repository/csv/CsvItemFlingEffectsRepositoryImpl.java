package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvItemFlingEffects;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvItemFlingEffectsRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvItemFlingEffects> {

    @Value("classpath:data/item_fling_effects.csv")
    private Resource csvFile;
    
    private final List<CsvItemFlingEffects> csvItemFlingEffects = new ArrayList<>();

    @Override
    public CsvItemFlingEffects getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.id(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvItemFlingEffects> getAll() {
        if (csvItemFlingEffects.isEmpty()) {
            try {
                csvItemFlingEffects.addAll(getCsvEntities(csvFile, CsvItemFlingEffects.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/item_fling_effects.csv", e);
            }
        }
        return csvItemFlingEffects;
    }
}
