package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvPokemonMoves;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvPokemonMovesRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvPokemonMoves> {

    @Value("classpath:data/pokemon_moves.csv")
    private Resource csvFile;
    
    private final List<CsvPokemonMoves> csvPokemonMoves = new ArrayList<>();

    @Override
    public CsvPokemonMoves getById(Long id) {
        throw new UnsupportedOperationException("csvpokemonmoves.csv has no single ID.");
    }

    @Override
    public List<CsvPokemonMoves> getAll() {
        if (csvPokemonMoves.isEmpty()) {
            try {
                csvPokemonMoves.addAll(getCsvEntities(csvFile, CsvPokemonMoves.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/pokemon_moves.csv", e);
            }
        }
        return csvPokemonMoves;
    }

    public List<CsvPokemonMoves> getByPokemonId(Long pokemonId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.pokemonId(), pokemonId))
                .toList();
    }

    public List<CsvPokemonMoves> getByVersionGroupId(Long versionGroupId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.versionGroupId(), versionGroupId))
                .toList();
    }

    public List<CsvPokemonMoves> getByMoveId(Long moveId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.moveId(), moveId))
                .toList();
    }

    public List<CsvPokemonMoves> getByMoveMethodId(Long moveMethodId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.moveMethodId(), moveMethodId))
                .toList();
    }
}
