package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvPokemonFormPokeathlonStats;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvPokemonFormPokeathlonStatsRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvPokemonFormPokeathlonStats> {

    @Value("classpath:data/pokemon_form_pokeathlon_stats.csv")
    private Resource csvFile;
    
    private final List<CsvPokemonFormPokeathlonStats> csvPokemonFormPokeathlonStats = new ArrayList<>();

    @Override
    public CsvPokemonFormPokeathlonStats getById(Long id) {
        throw new UnsupportedOperationException("csvpokemonformpokeathlonstats.csv has no single ID.");
    }

    @Override
    public List<CsvPokemonFormPokeathlonStats> getAll() {
        if (csvPokemonFormPokeathlonStats.isEmpty()) {
            try {
                csvPokemonFormPokeathlonStats.addAll(getCsvEntities(csvFile, CsvPokemonFormPokeathlonStats.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/pokemon_form_pokeathlon_stats.csv", e);
            }
        }
        return csvPokemonFormPokeathlonStats;
    }

    public List<CsvPokemonFormPokeathlonStats> getByPokemonFormId(Long pokemonFormId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getPokemonFormId(), pokemonFormId))
                .toList();
    }

    public List<CsvPokemonFormPokeathlonStats> getByPokeathlonStatId(Long pokeathlonStatId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getPokeathlonStatId(), pokeathlonStatId))
                .toList();
    }
}
