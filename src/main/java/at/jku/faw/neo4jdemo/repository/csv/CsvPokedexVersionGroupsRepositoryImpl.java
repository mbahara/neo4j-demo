package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvPokedexVersionGroups;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvPokedexVersionGroupsRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvPokedexVersionGroups> {

    @Value("classpath:data/pokedex_version_groups.csv")
    private Resource csvFile;
    
    private final List<CsvPokedexVersionGroups> csvPokedexVersionGroups = new ArrayList<>();

    @Override
    public CsvPokedexVersionGroups getById(Long id) {
        throw new UnsupportedOperationException("csvpokedexversiongroups.csv has no single ID.");
    }

    @Override
    public List<CsvPokedexVersionGroups> getAll() {
        if (csvPokedexVersionGroups.isEmpty()) {
            try {
                csvPokedexVersionGroups.addAll(getCsvEntities(csvFile, CsvPokedexVersionGroups.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/pokedex_version_groups.csv", e);
            }
        }
        return csvPokedexVersionGroups;
    }

    public List<CsvPokedexVersionGroups> getByPokedexId(Long pokedexId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.pokedexId(), pokedexId))
                .toList();
    }

    public List<CsvPokedexVersionGroups> getByVersionGroupId(Long versionGroupId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.versionGroupId(), versionGroupId))
                .toList();
    }
}
