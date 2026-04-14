package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvAbilityGenerations;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvAbilityGenerationsRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvAbilityGenerations> {

    @Value("classpath:data/ability_generations.csv")
    private Resource csvFile;
    
    private final List<CsvAbilityGenerations> csvAbilityGenerations = new ArrayList<>();

    @Override
    public CsvAbilityGenerations getById(Long id) {
        throw new UnsupportedOperationException("ability_generations.csv has no single ID. Use findByAbilityAndGeneration.");
    }

    public List<CsvAbilityGenerations> getByAbilityId(Long abilityId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getAbilityId(), abilityId))
                .toList();
    }

    public List<CsvAbilityGenerations> getByGenerationId(Long genId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getGenerationId(), genId))
                .toList();
    }

    @Override
    public List<CsvAbilityGenerations> getAll() {
        if (csvAbilityGenerations.isEmpty()) {
            try {
                csvAbilityGenerations.addAll(getCsvEntities(csvFile, CsvAbilityGenerations.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/ability_generations.csv", e);
            }
        }
        return csvAbilityGenerations;
    }
}
