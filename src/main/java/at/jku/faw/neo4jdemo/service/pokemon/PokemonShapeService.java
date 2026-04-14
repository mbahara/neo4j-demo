package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonShapeRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.PokemonShapeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PokemonShapeService implements IPokemonDataLoader {

    private final CsvPokemonShapeRepositoryImpl csvPokemonShapeRepository;
    private final PokemonShapeRepository pokemonShapeRepository;

    public PokemonShapeService(CsvPokemonShapeRepositoryImpl csvPokemonShapeRepository,
                           PokemonShapeRepository neo4jRepo) {
        this.csvPokemonShapeRepository = csvPokemonShapeRepository;
        this.pokemonShapeRepository = neo4jRepo;
    }

    @Override
    public String getEntityName() { return "PokemonShape"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvPokemonShapeRepository.getAll().forEach(csv -> {
            pokemonShapeRepository.insertPokemonShape(csv.pokemonShapeId(), csv.name(), csv.awesomeName(), csv.description());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        // No relationships to load.
    }
}
