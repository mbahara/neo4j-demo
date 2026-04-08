package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvPokemonMoveMethods;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvPokemonMoveMethodsRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvPokemonMoveMethods> {

    @Value("classpath:data/pokemon_move_methods.csv")
    private Resource csvFile;
    
    private final List<CsvPokemonMoveMethods> csvPokemonMoveMethods = new ArrayList<>();

    @Override
    public CsvPokemonMoveMethods getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.id(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvPokemonMoveMethods> getAll() {
        if (csvPokemonMoveMethods.isEmpty()) {
            try {
                csvPokemonMoveMethods.addAll(getCsvEntities(csvFile, CsvPokemonMoveMethods.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/pokemon_move_methods.csv", e);
            }
        }
        return csvPokemonMoveMethods;
    }
}
