package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvItemPockets;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvItemPocketsRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvItemPockets> {

    @Value("classpath:data/item_pockets.csv")
    private Resource csvFile;
    
    private final List<CsvItemPockets> csvItemPockets = new ArrayList<>();

    @Override
    public CsvItemPockets getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.getId(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvItemPockets> getAll() {
        if (csvItemPockets.isEmpty()) {
            try {
                csvItemPockets.addAll(getCsvEntities(csvFile, CsvItemPockets.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/item_pockets.csv", e);
            }
        }
        return csvItemPockets;
    }
}
