package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonColorsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.PokemonColorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PokemonColorService implements IPokemonDataLoader {

    private final CsvPokemonColorsRepositoryImpl csvPokemonColorsRepository;
    private final PokemonColorRepository pokemonColorRepository;

    public PokemonColorService(CsvPokemonColorsRepositoryImpl csvPokemonColorsRepository,
                           PokemonColorRepository pokemonColorRepository) {
        this.csvPokemonColorsRepository = csvPokemonColorsRepository;
        this.pokemonColorRepository = pokemonColorRepository;
    }

    @Override
    public String getEntityName() { return "PokemonColor"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvPokemonColorsRepository.getAll().forEach(csv -> {
            pokemonColorRepository.insertPokemonColor(csv.id(), csv.identifier());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
