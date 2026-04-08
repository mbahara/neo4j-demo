package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvPokemonShape;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvPokemonShapeRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvPokemonShape> {

    @Value("classpath:data/pokemon_shape.csv")
    private Resource csvFile;
    
    private final List<CsvPokemonShape> csvPokemonShape = new ArrayList<>();

    @Override
    public CsvPokemonShape getById(Long id) {
        throw new UnsupportedOperationException("csvpokemonshape.csv has no single ID.");
    }

    @Override
    public List<CsvPokemonShape> getAll() {
        if (csvPokemonShape.isEmpty()) {
            try {
                csvPokemonShape.addAll(getCsvEntities(csvFile, CsvPokemonShape.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/pokemon_shape.csv", e);
            }
        }
        return csvPokemonShape;
    }

    public List<CsvPokemonShape> getByPokemonShapeId(Long pokemonShapeId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.pokemonShapeId(), pokemonShapeId))
                .toList();
    }
}
