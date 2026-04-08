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
public class CsvSpeciesRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvPokemonSpecies> {

	@Value("classpath:data/pokemon-species.csv")
	private Resource speciesCsv;
	private final List<CsvPokemonSpecies> csvSpecies = new ArrayList<>();

	public CsvSpeciesRepositoryImpl() throws IOException {
		csvSpecies.addAll(getCsvEntities(speciesCsv, CsvPokemonSpecies.class));
	}

	@Override
	public CsvPokemonSpecies getById(Long id) {
		return csvSpecies.stream().filter(p -> Objects.equals(p.id(), id)).findFirst().orElse(null);
	}

	@Override
	public List<CsvPokemonSpecies> getAll() {
		try {
			return getCsvEntities(speciesCsv, CsvPokemonSpecies.class);
		} catch (IOException e) {
			throw new UncheckedIOException("Failed to read CSV: data/pokemon-species.csv", e);
		}

	}

    public List<CsvPokemonSpecies> getByGenerationId(Long generationId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.generationId(), generationId))
                .toList();
    }

    public List<CsvPokemonSpecies> getByEvolvesFromSpeciesId(Long evolvesFromSpeciesId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.evolvesFromSpeciesId(), evolvesFromSpeciesId))
                .toList();
    }

    public List<CsvPokemonSpecies> getByEvolutionChainId(Long evolutionChainId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.evolutionChainId(), evolutionChainId))
                .toList();
    }

    public List<CsvPokemonSpecies> getByColorId(Long colorId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.colorId(), colorId))
                .toList();
    }

    public List<CsvPokemonSpecies> getByShapeId(Long shapeId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.shapeId(), shapeId))
                .toList();
    }

    public List<CsvPokemonSpecies> getByHabitatId(Long habitatId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.habitatId(), habitatId))
                .toList();
    }

    public List<CsvPokemonSpecies> getByGrowthRateId(Long growthRateId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.growthRateId(), growthRateId))
                .toList();
    }
}
