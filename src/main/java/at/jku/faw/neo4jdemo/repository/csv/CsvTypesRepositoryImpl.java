package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvTypes;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvTypesRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvTypes> {

    @Value("classpath:data/types.csv")
    private Resource csvFile;
    
    private final List<CsvTypes> csvTypes = new ArrayList<>();

    @Override
    public CsvTypes getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.id(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvTypes> getAll() {
        if (csvTypes.isEmpty()) {
            try {
                csvTypes.addAll(getCsvEntities(csvFile, CsvTypes.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/types.csv", e);
            }
        }
        return csvTypes;
    }

    public List<CsvTypes> getByGenerationId(Long generationId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.generationId(), generationId))
                .toList();
    }

    public List<CsvTypes> getByDamageClassId(Long damageClassId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.damageClassId(), damageClassId))
                .toList();
    }
}
