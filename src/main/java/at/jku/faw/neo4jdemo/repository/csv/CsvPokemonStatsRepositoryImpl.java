package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvPokemonStats;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvPokemonStatsRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvPokemonStats> {

    @Value("classpath:data/pokemon_stats.csv")
    private Resource csvFile;
    
    private final List<CsvPokemonStats> csvPokemonStats = new ArrayList<>();

    @Override
    public CsvPokemonStats getById(Long id) {
        throw new UnsupportedOperationException("csvpokemonstats.csv has no single ID.");
    }

    @Override
    public List<CsvPokemonStats> getAll() {
        if (csvPokemonStats.isEmpty()) {
            try {
                csvPokemonStats.addAll(getCsvEntities(csvFile, CsvPokemonStats.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/pokemon_stats.csv", e);
            }
        }
        return csvPokemonStats;
    }

    public List<CsvPokemonStats> getByPokemonId(Long pokemonId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.pokemonId(), pokemonId))
                .toList();
    }

    public List<CsvPokemonStats> getByStatId(Long statId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.statId(), statId))
                .toList();
    }
}
