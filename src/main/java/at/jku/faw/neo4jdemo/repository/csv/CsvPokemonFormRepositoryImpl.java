package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvPokemonForm;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvPokemonFormRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvPokemonForm> {

    @Value("classpath:data/pokemon_forms.csv")
    private Resource csvFile;
    
    private final List<CsvPokemonForm> csvPokemonForm = new ArrayList<>();

    @Override
    public CsvPokemonForm getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.getId(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvPokemonForm> getAll() {
        if (csvPokemonForm.isEmpty()) {
            try {
                csvPokemonForm.addAll(getCsvEntities(csvFile, CsvPokemonForm.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/pokemon_forms.csv", e);
            }
        }
        return csvPokemonForm;
    }

    public List<CsvPokemonForm> getByPokemonId(Long pokemonId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getPokemonId(), pokemonId))
                .toList();
    }
}
