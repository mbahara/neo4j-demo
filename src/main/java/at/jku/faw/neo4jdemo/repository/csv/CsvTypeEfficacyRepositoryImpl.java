package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvTypeEfficacy;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvTypeEfficacyRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvTypeEfficacy> {

    @Value("classpath:data/type_efficacy.csv")
    private Resource csvFile;
    
    private final List<CsvTypeEfficacy> csvTypeEfficacy = new ArrayList<>();

    @Override
    public CsvTypeEfficacy getById(Long id) {
        throw new UnsupportedOperationException("csvtypeefficacy.csv has no single ID.");
    }

    @Override
    public List<CsvTypeEfficacy> getAll() {
        if (csvTypeEfficacy.isEmpty()) {
            try {
                csvTypeEfficacy.addAll(getCsvEntities(csvFile, CsvTypeEfficacy.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/type_efficacy.csv", e);
            }
        }
        return csvTypeEfficacy;
    }

    public List<CsvTypeEfficacy> getByDamageTypeId(Long damageTypeId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.damageTypeId(), damageTypeId))
                .toList();
    }

    public List<CsvTypeEfficacy> getByTargetTypeId(Long targetTypeId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.targetTypeId(), targetTypeId))
                .toList();
    }
}
