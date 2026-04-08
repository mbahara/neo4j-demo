package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvContestTypes;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvContestTypesRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvContestTypes> {

    @Value("classpath:data/contest_types.csv")
    private Resource csvFile;
    
    private final List<CsvContestTypes> csvContestTypes = new ArrayList<>();

    @Override
    public CsvContestTypes getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.id(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvContestTypes> getAll() {
        if (csvContestTypes.isEmpty()) {
            try {
                csvContestTypes.addAll(getCsvEntities(csvFile, CsvContestTypes.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/contest_types.csv", e);
            }
        }
        return csvContestTypes;
    }
}
