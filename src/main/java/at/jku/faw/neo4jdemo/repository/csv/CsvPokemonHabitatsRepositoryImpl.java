package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvPokemonHabitats;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvPokemonHabitatsRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvPokemonHabitats> {

    @Value("classpath:data/pokemon_habitats.csv")
    private Resource csvFile;
    
    private final List<CsvPokemonHabitats> csvPokemonHabitats = new ArrayList<>();

    @Override
    public CsvPokemonHabitats getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.id(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvPokemonHabitats> getAll() {
        if (csvPokemonHabitats.isEmpty()) {
            try {
                csvPokemonHabitats.addAll(getCsvEntities(csvFile, CsvPokemonHabitats.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/pokemon_habitats.csv", e);
            }
        }
        return csvPokemonHabitats;
    }
}
