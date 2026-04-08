package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvItemFlags;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvItemFlagsRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvItemFlags> {

    @Value("classpath:data/item_flags.csv")
    private Resource csvFile;
    
    private final List<CsvItemFlags> csvItemFlags = new ArrayList<>();

    @Override
    public CsvItemFlags getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.id(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvItemFlags> getAll() {
        if (csvItemFlags.isEmpty()) {
            try {
                csvItemFlags.addAll(getCsvEntities(csvFile, CsvItemFlags.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/item_flags.csv", e);
            }
        }
        return csvItemFlags;
    }
}
