package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvPokemonDexNumbers;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvPokemonDexNumbersRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvPokemonDexNumbers> {

    @Value("classpath:data/pokemon_dex_numbers.csv")
    private Resource csvFile;
    
    private final List<CsvPokemonDexNumbers> csvPokemonDexNumbers = new ArrayList<>();

    @Override
    public CsvPokemonDexNumbers getById(Long id) {
        throw new UnsupportedOperationException("csvpokemondexnumbers.csv has no single ID.");
    }

    @Override
    public List<CsvPokemonDexNumbers> getAll() {
        if (csvPokemonDexNumbers.isEmpty()) {
            try {
                csvPokemonDexNumbers.addAll(getCsvEntities(csvFile, CsvPokemonDexNumbers.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/pokemon_dex_numbers.csv", e);
            }
        }
        return csvPokemonDexNumbers;
    }

    public List<CsvPokemonDexNumbers> getBySpeciesId(Long speciesId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getSpeciesId(), speciesId))
                .toList();
    }

    public List<CsvPokemonDexNumbers> getByPokedexId(Long pokedexId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getPokedexId(), pokedexId))
                .toList();
    }
}
