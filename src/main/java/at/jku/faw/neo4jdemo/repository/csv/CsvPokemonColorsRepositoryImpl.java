package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvPokemonColors;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvPokemonColorsRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvPokemonColors> {

    @Value("classpath:data/pokemon_colors.csv")
    private Resource csvFile;
    
    private final List<CsvPokemonColors> csvPokemonColors = new ArrayList<>();

    @Override
    public CsvPokemonColors getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.id(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvPokemonColors> getAll() {
        if (csvPokemonColors.isEmpty()) {
            try {
                csvPokemonColors.addAll(getCsvEntities(csvFile, CsvPokemonColors.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/pokemon_colors.csv", e);
            }
        }
        return csvPokemonColors;
    }
}
