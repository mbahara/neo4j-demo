package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvPokemonGameIndices;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvPokemonGameIndicesRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvPokemonGameIndices> {

    @Value("classpath:data/pokemon_game_indices.csv")
    private Resource csvFile;
    
    private final List<CsvPokemonGameIndices> csvPokemonGameIndices = new ArrayList<>();

    @Override
    public CsvPokemonGameIndices getById(Long id) {
        throw new UnsupportedOperationException("csvpokemongameindices.csv has no single ID.");
    }

    @Override
    public List<CsvPokemonGameIndices> getAll() {
        if (csvPokemonGameIndices.isEmpty()) {
            try {
                csvPokemonGameIndices.addAll(getCsvEntities(csvFile, CsvPokemonGameIndices.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/pokemon_game_indices.csv", e);
            }
        }
        return csvPokemonGameIndices;
    }

    public List<CsvPokemonGameIndices> getByPokemonId(Long pokemonId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getPokemonId(), pokemonId))
                .toList();
    }

    public List<CsvPokemonGameIndices> getByVersionId(Long versionId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getVersionId(), versionId))
                .toList();
    }
}
