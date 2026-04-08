package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvSuperContestCombos;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvSuperContestCombosRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvSuperContestCombos> {

    @Value("classpath:data/super_contest_combos.csv")
    private Resource csvFile;
    
    private final List<CsvSuperContestCombos> csvSuperContestCombos = new ArrayList<>();

    @Override
    public CsvSuperContestCombos getById(Long id) {
        throw new UnsupportedOperationException("csvsupercontestcombos.csv has no single ID.");
    }

    @Override
    public List<CsvSuperContestCombos> getAll() {
        if (csvSuperContestCombos.isEmpty()) {
            try {
                csvSuperContestCombos.addAll(getCsvEntities(csvFile, CsvSuperContestCombos.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/super_contest_combos.csv", e);
            }
        }
        return csvSuperContestCombos;
    }

    public List<CsvSuperContestCombos> getByFirstMoveId(Long firstMoveId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.firstMoveId(), firstMoveId))
                .toList();
    }

    public List<CsvSuperContestCombos> getBySecondMoveId(Long secondMoveId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.secondMoveId(), secondMoveId))
                .toList();
    }
}
