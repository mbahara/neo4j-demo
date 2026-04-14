package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvAbility;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvAbilitiesRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvAbility> {

    @Value("classpath:data/abilities.csv")
    private Resource csvFile;
    
    private final List<CsvAbility> csvAbilities = new ArrayList<>();

    @Override
    public CsvAbility getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.getId(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvAbility> getAll() {
        if (csvAbilities.isEmpty()) {
            try {
                csvAbilities.addAll(getCsvEntities(csvFile, CsvAbility.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/abilities.csv", e);
            }
        }
        return csvAbilities;
    }
}
