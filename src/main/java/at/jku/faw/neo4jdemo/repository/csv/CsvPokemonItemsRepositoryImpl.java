package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvPokemonItems;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvPokemonItemsRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvPokemonItems> {

    @Value("classpath:data/pokemon_items.csv")
    private Resource csvFile;
    
    private final List<CsvPokemonItems> csvPokemonItems = new ArrayList<>();

    @Override
    public CsvPokemonItems getById(Long id) {
        throw new UnsupportedOperationException("csvpokemonitems.csv has no single ID.");
    }

    @Override
    public List<CsvPokemonItems> getAll() {
        if (csvPokemonItems.isEmpty()) {
            try {
                csvPokemonItems.addAll(getCsvEntities(csvFile, CsvPokemonItems.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/pokemon_items.csv", e);
            }
        }
        return csvPokemonItems;
    }

    public List<CsvPokemonItems> getByPokemonId(Long pokemonId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.pokemonId(), pokemonId))
                .toList();
    }

    public List<CsvPokemonItems> getByVersionId(Long versionId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.versionId(), versionId))
                .toList();
    }

    public List<CsvPokemonItems> getByItemId(Long itemId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.itemId(), itemId))
                .toList();
    }
}
