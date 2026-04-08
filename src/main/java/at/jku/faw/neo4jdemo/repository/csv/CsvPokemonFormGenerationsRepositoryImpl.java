package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvPokemonFormGenerations;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvPokemonFormGenerationsRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvPokemonFormGenerations> {

    @Value("classpath:data/pokemon_form_generations.csv")
    private Resource csvFile;
    
    private final List<CsvPokemonFormGenerations> csvPokemonFormGenerations = new ArrayList<>();

    @Override
    public CsvPokemonFormGenerations getById(Long id) {
        throw new UnsupportedOperationException("csvpokemonformgenerations.csv has no single ID.");
    }

    @Override
    public List<CsvPokemonFormGenerations> getAll() {
        if (csvPokemonFormGenerations.isEmpty()) {
            try {
                csvPokemonFormGenerations.addAll(getCsvEntities(csvFile, CsvPokemonFormGenerations.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/pokemon_form_generations.csv", e);
            }
        }
        return csvPokemonFormGenerations;
    }

    public List<CsvPokemonFormGenerations> getByPokemonFormId(Long pokemonFormId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.pokemonFormId(), pokemonFormId))
                .toList();
    }

    public List<CsvPokemonFormGenerations> getByGenerationId(Long generationId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.generationId(), generationId))
                .toList();
    }
}
