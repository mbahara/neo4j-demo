package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvPokemonType;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvPokemonTypeRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvPokemonType> {

    @Value("classpath:data/pokemon_types.csv")
    private Resource csvFile;
    
    private final List<CsvPokemonType> csvPokemonType = new ArrayList<>();

    @Override
    public CsvPokemonType getById(Long id) {
        throw new UnsupportedOperationException("csvpokemontype.csv has no single ID.");
    }

    @Override
    public List<CsvPokemonType> getAll() {
        if (csvPokemonType.isEmpty()) {
            try {
                csvPokemonType.addAll(getCsvEntities(csvFile, CsvPokemonType.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/pokemon_types.csv", e);
            }
        }
        return csvPokemonType;
    }

    public List<CsvPokemonType> getByPokemonId(Long pokemonId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getPokemonId(), pokemonId))
                .toList();
    }

    public List<CsvPokemonType> getByTypeId(Long typeId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getTypeId(), typeId))
                .toList();
    }
}
