package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvExperience;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvExperienceRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvExperience> {

    @Value("classpath:data/experience.csv")
    private Resource csvFile;
    
    private final List<CsvExperience> csvExperience = new ArrayList<>();

    @Override
    public CsvExperience getById(Long id) {
        throw new UnsupportedOperationException("csvexperience.csv has no single ID.");
    }

    @Override
    public List<CsvExperience> getAll() {
        if (csvExperience.isEmpty()) {
            try {
                csvExperience.addAll(getCsvEntities(csvFile, CsvExperience.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/experience.csv", e);
            }
        }
        return csvExperience;
    }

    public List<CsvExperience> getByGrowthRateId(Long growthRateId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getGrowthRateId(), growthRateId))
                .toList();
    }
}
