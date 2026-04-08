package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvMachine;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvMachineRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvMachine> {

    @Value("classpath:data/machines.csv")
    private Resource csvFile;
    
    private final List<CsvMachine> csvMachine = new ArrayList<>();

    @Override
    public CsvMachine getById(Long id) {
        throw new UnsupportedOperationException("csvmachine.csv has no single ID.");
    }

    @Override
    public List<CsvMachine> getAll() {
        if (csvMachine.isEmpty()) {
            try {
                csvMachine.addAll(getCsvEntities(csvFile, CsvMachine.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/machines.csv", e);
            }
        }
        return csvMachine;
    }

    public List<CsvMachine> getByVersionGroupId(Long versionGroupId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.versionGroupId(), versionGroupId))
                .toList();
    }

    public List<CsvMachine> getByItemId(Long itemId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.itemId(), itemId))
                .toList();
    }

    public List<CsvMachine> getByMoveId(Long moveId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.moveId(), moveId))
                .toList();
    }
}
