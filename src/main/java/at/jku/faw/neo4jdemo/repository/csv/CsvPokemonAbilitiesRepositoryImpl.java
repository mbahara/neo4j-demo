package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvPokemonAbilities;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvPokemonAbilitiesRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvPokemonAbilities> {

    @Value("classpath:data/pokemon_abilities.csv")
    private Resource csvFile;
    
    private final List<CsvPokemonAbilities> csvPokemonAbilities = new ArrayList<>();

    @Override
    public CsvPokemonAbilities getById(Long id) {
        throw new UnsupportedOperationException("csvpokemonabilities.csv has no single ID.");
    }

    @Override
    public List<CsvPokemonAbilities> getAll() {
        if (csvPokemonAbilities.isEmpty()) {
            try {
                csvPokemonAbilities.addAll(getCsvEntities(csvFile, CsvPokemonAbilities.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/pokemon_abilities.csv", e);
            }
        }
        return csvPokemonAbilities;
    }

    public List<CsvPokemonAbilities> getByPokemonId(Long pokemonId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.pokemonId(), pokemonId))
                .toList();
    }

    public List<CsvPokemonAbilities> getByAbilityId(Long abilityId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.abilityId(), abilityId))
                .toList();
    }
}
