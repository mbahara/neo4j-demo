package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvContestCombos;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvContestCombosRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvContestCombos> {

    @Value("classpath:data/contest_combos.csv")
    private Resource csvFile;
    
    private final List<CsvContestCombos> csvContestCombos = new ArrayList<>();

    @Override
    public CsvContestCombos getById(Long id) {
        throw new UnsupportedOperationException("ability_generations.csv has no single ID. Use findByAbilityAndGeneration.");
    }

    @Override
    public List<CsvContestCombos> getAll() {
        if (csvContestCombos.isEmpty()) {
            try {
                csvContestCombos.addAll(getCsvEntities(csvFile, CsvContestCombos.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/contest_combos.csv", e);
            }
        }
        return csvContestCombos;
    }

    public List<CsvContestCombos> getByFirstMoveId(Long firstMoveId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.firstMoveId(), firstMoveId))
                .toList();
    }

    public List<CsvContestCombos> getBySecondMoveId(Long secondMoveId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.secondMoveId(), secondMoveId))
                .toList();
    }
}
