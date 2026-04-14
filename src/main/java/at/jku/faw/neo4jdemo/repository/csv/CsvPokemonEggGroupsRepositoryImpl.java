package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvPokemonEggGroups;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvPokemonEggGroupsRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvPokemonEggGroups> {

    @Value("classpath:data/pokemon_egg_groups.csv")
    private Resource csvFile;
    
    private final List<CsvPokemonEggGroups> csvPokemonEggGroups = new ArrayList<>();

    @Override
    public CsvPokemonEggGroups getById(Long id) {
        throw new UnsupportedOperationException("csvpokemonegggroups.csv has no single ID.");
    }

    @Override
    public List<CsvPokemonEggGroups> getAll() {
        if (csvPokemonEggGroups.isEmpty()) {
            try {
                csvPokemonEggGroups.addAll(getCsvEntities(csvFile, CsvPokemonEggGroups.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/pokemon_egg_groups.csv", e);
            }
        }
        return csvPokemonEggGroups;
    }

    public List<CsvPokemonEggGroups> getBySpeciesId(Long speciesId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getSpeciesId(), speciesId))
                .toList();
    }

    public List<CsvPokemonEggGroups> getByEggGroupId(Long eggGroupId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getEggGroupId(), eggGroupId))
                .toList();
    }
}
