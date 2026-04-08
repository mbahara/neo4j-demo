package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvGrowthRates;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvGrowthRatesRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvGrowthRates> {

    @Value("classpath:data/growth_rates.csv")
    private Resource csvFile;
    
    private final List<CsvGrowthRates> csvGrowthRates = new ArrayList<>();

    @Override
    public CsvGrowthRates getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.id(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvGrowthRates> getAll() {
        if (csvGrowthRates.isEmpty()) {
            try {
                csvGrowthRates.addAll(getCsvEntities(csvFile, CsvGrowthRates.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/growth_rates.csv", e);
            }
        }
        return csvGrowthRates;
    }
}
