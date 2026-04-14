package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvMoveBattleStyles;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvMoveBattleStylesRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvMoveBattleStyles> {

    @Value("classpath:data/move_battle_styles.csv")
    private Resource csvFile;
    
    private final List<CsvMoveBattleStyles> csvMoveBattleStyles = new ArrayList<>();

    @Override
    public CsvMoveBattleStyles getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.getId(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvMoveBattleStyles> getAll() {
        if (csvMoveBattleStyles.isEmpty()) {
            try {
                csvMoveBattleStyles.addAll(getCsvEntities(csvFile, CsvMoveBattleStyles.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/move_battle_styles.csv", e);
            }
        }
        return csvMoveBattleStyles;
    }
}
