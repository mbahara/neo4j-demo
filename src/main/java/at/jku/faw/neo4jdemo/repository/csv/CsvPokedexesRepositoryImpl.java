package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvPokedexes;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvPokedexesRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvPokedexes> {

    @Value("classpath:data/pokedexes.csv")
    private Resource csvFile;
    
    private final List<CsvPokedexes> csvPokedexes = new ArrayList<>();

    @Override
    public CsvPokedexes getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.id(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvPokedexes> getAll() {
        if (csvPokedexes.isEmpty()) {
            try {
                csvPokedexes.addAll(getCsvEntities(csvFile, CsvPokedexes.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/pokedexes.csv", e);
            }
        }
        return csvPokedexes;
    }

    public List<CsvPokedexes> getByRegionId(Long regionId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.regionId(), regionId))
                .toList();
    }
}
