package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvMoveChangelog;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvMoveChangelogRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvMoveChangelog> {

    @Value("classpath:data/move_changelog.csv")
    private Resource csvFile;
    
    private final List<CsvMoveChangelog> csvMoveChangelog = new ArrayList<>();

    @Override
    public CsvMoveChangelog getById(Long id) {
        throw new UnsupportedOperationException("csvmovechangelog.csv has no single ID.");
    }

    @Override
    public List<CsvMoveChangelog> getAll() {
        if (csvMoveChangelog.isEmpty()) {
            try {
                csvMoveChangelog.addAll(getCsvEntities(csvFile, CsvMoveChangelog.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/move_changelog.csv", e);
            }
        }
        return csvMoveChangelog;
    }

    public List<CsvMoveChangelog> getByMoveId(Long moveId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.moveId(), moveId))
                .toList();
    }

    public List<CsvMoveChangelog> getByVersionGroupId(Long versionGroupId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.versionGroupId(), versionGroupId))
                .toList();
    }

    public List<CsvMoveChangelog> getByTypeId(Long typeId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.typeId(), typeId))
                .toList();
    }

    public List<CsvMoveChangelog> getByTargetId(Long targetId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.targetId(), targetId))
                .toList();
    }

    public List<CsvMoveChangelog> getByEffectId(Long effectId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.effectId(), effectId))
                .toList();
    }
}
