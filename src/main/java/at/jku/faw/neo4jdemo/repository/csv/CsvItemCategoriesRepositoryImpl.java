package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvItemCategories;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvItemCategoriesRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvItemCategories> {

    @Value("classpath:data/item_categories.csv")
    private Resource csvFile;
    
    private final List<CsvItemCategories> csvItemCategories = new ArrayList<>();

    @Override
    public CsvItemCategories getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.getId(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvItemCategories> getAll() {
        if (csvItemCategories.isEmpty()) {
            try {
                csvItemCategories.addAll(getCsvEntities(csvFile, CsvItemCategories.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/item_categories.csv", e);
            }
        }
        return csvItemCategories;
    }

    public List<CsvItemCategories> getByPocketId(Long pocketId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getPocketId(), pocketId))
                .toList();
    }
}
