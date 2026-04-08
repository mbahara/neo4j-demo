package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvMoveMetaCategories;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvMoveMetaCategoriesRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvMoveMetaCategories> {

    @Value("classpath:data/move_meta_categories.csv")
    private Resource csvFile;
    
    private final List<CsvMoveMetaCategories> csvMoveMetaCategories = new ArrayList<>();

    @Override
    public CsvMoveMetaCategories getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.id(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvMoveMetaCategories> getAll() {
        if (csvMoveMetaCategories.isEmpty()) {
            try {
                csvMoveMetaCategories.addAll(getCsvEntities(csvFile, CsvMoveMetaCategories.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/move_meta_categories.csv", e);
            }
        }
        return csvMoveMetaCategories;
    }
}
