package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvEggGroups;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvEggGroupsRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvEggGroups> {

    @Value("classpath:data/egg_groups.csv")
    private Resource csvFile;
    
    private final List<CsvEggGroups> csvEggGroups = new ArrayList<>();

    @Override
    public CsvEggGroups getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.id(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvEggGroups> getAll() {
        if (csvEggGroups.isEmpty()) {
            try {
                csvEggGroups.addAll(getCsvEntities(csvFile, CsvEggGroups.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/egg_groups.csv", e);
            }
        }
        return csvEggGroups;
    }
}
