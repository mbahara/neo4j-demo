package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvEvolutionChains;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvEvolutionChainsRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvEvolutionChains> {

    @Value("classpath:data/evolution_chains.csv")
    private Resource csvFile;
    
    private final List<CsvEvolutionChains> csvEvolutionChains = new ArrayList<>();

    @Override
    public CsvEvolutionChains getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.getId(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvEvolutionChains> getAll() {
        if (csvEvolutionChains.isEmpty()) {
            try {
                csvEvolutionChains.addAll(getCsvEntities(csvFile, CsvEvolutionChains.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/evolution_chains.csv", e);
            }
        }
        return csvEvolutionChains;
    }

    public List<CsvEvolutionChains> getByBabyTriggerItemId(Long babyTriggerItemId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getBabyTriggerItemId(), babyTriggerItemId))
                .toList();
    }
}
