package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvMove;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvMoveRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvMove> {

    @Value("classpath:data/moves.csv")
    private Resource csvFile;
    
    private final List<CsvMove> csvMove = new ArrayList<>();

    @Override
    public CsvMove getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.id(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvMove> getAll() {
        if (csvMove.isEmpty()) {
            try {
                csvMove.addAll(getCsvEntities(csvFile, CsvMove.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/moves.csv", e);
            }
        }
        return csvMove;
    }

    public List<CsvMove> getByGenerationId(Long generationId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.generationId(), generationId))
                .toList();
    }

    public List<CsvMove> getByTypeId(Long typeId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.typeId(), typeId))
                .toList();
    }

    public List<CsvMove> getByTargetId(Long targetId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.targetId(), targetId))
                .toList();
    }

    public List<CsvMove> getByDamageClassId(Long damageClassId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.damageClassId(), damageClassId))
                .toList();
    }

    public List<CsvMove> getByEffectId(Long effectId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.effectId(), effectId))
                .toList();
    }
}
