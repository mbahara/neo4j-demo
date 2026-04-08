package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvGenders;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvGendersRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvGenders> {

    @Value("classpath:data/genders.csv")
    private Resource csvFile;
    
    private final List<CsvGenders> csvGenders = new ArrayList<>();

    @Override
    public CsvGenders getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.id(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvGenders> getAll() {
        if (csvGenders.isEmpty()) {
            try {
                csvGenders.addAll(getCsvEntities(csvFile, CsvGenders.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/genders.csv", e);
            }
        }
        return csvGenders;
    }
}
