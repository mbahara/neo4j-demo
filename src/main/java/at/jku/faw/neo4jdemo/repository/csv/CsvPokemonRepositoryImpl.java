package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvPokemon;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvPokemonRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvPokemon> {

    @Value("classpath:data/pokemon.csv")
    private Resource csvFile;
    
    private final List<CsvPokemon> csvPokemon = new ArrayList<>();

    @Override
    public CsvPokemon getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.getId(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvPokemon> getAll() {
        if (csvPokemon.isEmpty()) {
            try {
                csvPokemon.addAll(getCsvEntities(csvFile, CsvPokemon.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/pokemon.csv", e);
            }
        }
        return csvPokemon;
    }

    public List<CsvPokemon> getBySpeciesId(Long speciesId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getSpeciesId(), speciesId))
                .toList();
    }
}
