package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvEvolutionTriggers;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvEvolutionTriggersRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvEvolutionTriggers> {

    @Value("classpath:data/evolution_triggers.csv")
    private Resource csvFile;
    
    private final List<CsvEvolutionTriggers> csvEvolutionTriggers = new ArrayList<>();

    @Override
    public CsvEvolutionTriggers getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.getId(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvEvolutionTriggers> getAll() {
        if (csvEvolutionTriggers.isEmpty()) {
            try {
                csvEvolutionTriggers.addAll(getCsvEntities(csvFile, CsvEvolutionTriggers.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/evolution_triggers.csv", e);
            }
        }
        return csvEvolutionTriggers;
    }
}
