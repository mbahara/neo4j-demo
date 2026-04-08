package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvMoveDamageClasses;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvMoveDamageClassesRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvMoveDamageClasses> {

    @Value("classpath:data/move_damage_classes.csv")
    private Resource csvFile;
    
    private final List<CsvMoveDamageClasses> csvMoveDamageClasses = new ArrayList<>();

    @Override
    public CsvMoveDamageClasses getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.id(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvMoveDamageClasses> getAll() {
        if (csvMoveDamageClasses.isEmpty()) {
            try {
                csvMoveDamageClasses.addAll(getCsvEntities(csvFile, CsvMoveDamageClasses.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/move_damage_classes.csv", e);
            }
        }
        return csvMoveDamageClasses;
    }
}
