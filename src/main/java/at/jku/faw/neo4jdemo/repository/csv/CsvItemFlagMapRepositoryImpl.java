package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvItemFlagMap;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvItemFlagMapRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvItemFlagMap> {

    @Value("classpath:data/item_flag_map.csv")
    private Resource csvFile;
    
    private final List<CsvItemFlagMap> csvItemFlagMap = new ArrayList<>();

    @Override
    public CsvItemFlagMap getById(Long id) {
        throw new UnsupportedOperationException("csvitemflagmap.csv has no single ID.");
    }

    @Override
    public List<CsvItemFlagMap> getAll() {
        if (csvItemFlagMap.isEmpty()) {
            try {
                csvItemFlagMap.addAll(getCsvEntities(csvFile, CsvItemFlagMap.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/item_flag_map.csv", e);
            }
        }
        return csvItemFlagMap;
    }

    public List<CsvItemFlagMap> getByItemId(Long itemId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.itemId(), itemId))
                .toList();
    }

    public List<CsvItemFlagMap> getByItemFlagId(Long itemFlagId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.itemFlagId(), itemFlagId))
                .toList();
    }
}
