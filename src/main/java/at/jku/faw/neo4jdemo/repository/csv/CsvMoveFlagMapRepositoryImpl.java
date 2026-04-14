package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvMoveFlagMap;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvMoveFlagMapRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvMoveFlagMap> {

    @Value("classpath:data/move_flag_map.csv")
    private Resource csvFile;
    
    private final List<CsvMoveFlagMap> csvMoveFlagMap = new ArrayList<>();

    @Override
    public CsvMoveFlagMap getById(Long id) {
        throw new UnsupportedOperationException("csvmoveflagmap.csv has no single ID.");
    }

    @Override
    public List<CsvMoveFlagMap> getAll() {
        if (csvMoveFlagMap.isEmpty()) {
            try {
                csvMoveFlagMap.addAll(getCsvEntities(csvFile, CsvMoveFlagMap.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/move_flag_map.csv", e);
            }
        }
        return csvMoveFlagMap;
    }

    public List<CsvMoveFlagMap> getByMoveId(Long moveId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getMoveId(), moveId))
                .toList();
    }

    public List<CsvMoveFlagMap> getByMoveFlagId(Long moveFlagId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getMoveFlagId(), moveFlagId))
                .toList();
    }
}
