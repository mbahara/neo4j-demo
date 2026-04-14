package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvAbilityChangelog;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvAbilityChangelogRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvAbilityChangelog> {

    @Value("classpath:data/ability_changelog.csv")
    private Resource csvFile;
    
    private final List<CsvAbilityChangelog> csvAbilityChangelog = new ArrayList<>();

    @Override
    public CsvAbilityChangelog getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.getId(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvAbilityChangelog> getAll() {
        if (csvAbilityChangelog.isEmpty()) {
            try {
                csvAbilityChangelog.addAll(getCsvEntities(csvFile, CsvAbilityChangelog.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/ability_changelog.csv", e);
            }
        }
        return csvAbilityChangelog;
    }

    public List<CsvAbilityChangelog> getByAbilityId(Long abilityId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getAbilityId(), abilityId))
                .toList();
    }

    public List<CsvAbilityChangelog> getByVersionGroupId(Long versionGroupId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getVersionGroupId(), versionGroupId))
                .toList();
    }
}
