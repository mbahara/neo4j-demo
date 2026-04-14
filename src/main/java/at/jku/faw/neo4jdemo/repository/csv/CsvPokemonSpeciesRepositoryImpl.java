package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvPokemonSpecies;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvPokemonSpeciesRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvPokemonSpecies> {

    @Value("classpath:data/pokemon_species.csv")
    private Resource csvFile;
    
    private final List<CsvPokemonSpecies> csvPokemonSpecies = new ArrayList<>();

    @Override
    public CsvPokemonSpecies getById(Long id) {
        return getAll().stream().filter(e -> Objects.equals(e.getId(), id)).findFirst().orElse(null);
    }

    @Override
    public List<CsvPokemonSpecies> getAll() {
        if (csvPokemonSpecies.isEmpty()) {
            try {
                csvPokemonSpecies.addAll(getCsvEntities(csvFile, CsvPokemonSpecies.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/pokemon_species.csv", e);
            }
        }
        return csvPokemonSpecies;
    }

    public List<CsvPokemonSpecies> getByGenerationId(Long generationId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getGenerationId(), generationId))
                .toList();
    }

    public List<CsvPokemonSpecies> getByEvolvesFromSpeciesId(Long evolvesFromSpeciesId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getEvolvesFromSpeciesId(), evolvesFromSpeciesId))
                .toList();
    }

    public List<CsvPokemonSpecies> getByEvolutionChainId(Long evolutionChainId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getEvolutionChainId(), evolutionChainId))
                .toList();
    }

    public List<CsvPokemonSpecies> getByColorId(Long colorId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getColorId(), colorId))
                .toList();
    }

    public List<CsvPokemonSpecies> getByShapeId(Long shapeId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getShapeId(), shapeId))
                .toList();
    }

    public List<CsvPokemonSpecies> getByHabitatId(Long habitatId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getHabitatId(), habitatId))
                .toList();
    }

    public List<CsvPokemonSpecies> getByGrowthRateId(Long growthRateId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.getGrowthRateId(), growthRateId))
                .toList();
    }
}
