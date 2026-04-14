package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvVersionGroupPokemonMoveMethods;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvVersionGroupPokemonMoveMethodsRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvVersionGroupPokemonMoveMethods> {

    @Value("classpath:data/version_group_pokemon_move_methods.csv")
    private Resource csvFile;
    
    private final List<CsvVersionGroupPokemonMoveMethods> csvVersionGroupPokemonMoveMethods = new ArrayList<>();

    @Override
    public CsvVersionGroupPokemonMoveMethods getById(Long id) {
        throw new UnsupportedOperationException("csvversiongrouppokemonmovemethods.csv has no single ID.");
    }

    @Override
    public List<CsvVersionGroupPokemonMoveMethods> getAll() {
        if (csvVersionGroupPokemonMoveMethods.isEmpty()) {
            try {
                csvVersionGroupPokemonMoveMethods.addAll(getCsvEntities(csvFile, CsvVersionGroupPokemonMoveMethods.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/version_group_pokemon_move_methods.csv", e);
            }
        }
        return csvVersionGroupPokemonMoveMethods;
    }

    public List<CsvVersionGroupPokemonMoveMethods> getByVersionGroupId(Long versionGroupId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getVersionGroupId(), versionGroupId))
                .toList();
    }

    public List<CsvVersionGroupPokemonMoveMethods> getByPokemonMoveMethodId(Long pokemonMoveMethodId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getPokemonMoveMethodId(), pokemonMoveMethodId))
                .toList();
    }
}
