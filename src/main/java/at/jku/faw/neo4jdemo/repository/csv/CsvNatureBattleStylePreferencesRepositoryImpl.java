package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvNatureBattleStylePreferences;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvNatureBattleStylePreferencesRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvNatureBattleStylePreferences> {

    @Value("classpath:data/nature_battle_style_preferences.csv")
    private Resource csvFile;
    
    private final List<CsvNatureBattleStylePreferences> csvNatureBattleStylePreferences = new ArrayList<>();

    @Override
    public CsvNatureBattleStylePreferences getById(Long id) {
        throw new UnsupportedOperationException("csvnaturebattlestylepreferences.csv has no single ID.");
    }

    @Override
    public List<CsvNatureBattleStylePreferences> getAll() {
        if (csvNatureBattleStylePreferences.isEmpty()) {
            try {
                csvNatureBattleStylePreferences.addAll(getCsvEntities(csvFile, CsvNatureBattleStylePreferences.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/nature_battle_style_preferences.csv", e);
            }
        }
        return csvNatureBattleStylePreferences;
    }

    public List<CsvNatureBattleStylePreferences> getByNatureId(Long natureId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getNatureId(), natureId))
                .toList();
    }

    public List<CsvNatureBattleStylePreferences> getByMoveBattleStyleId(Long moveBattleStyleId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getMoveBattleStyleId(), moveBattleStyleId))
                .toList();
    }
}
