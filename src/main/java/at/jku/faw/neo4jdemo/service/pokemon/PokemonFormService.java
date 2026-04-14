package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvPokemonForm;
import at.jku.faw.neo4jdemo.model.neo4j.FormPokeathlonStats;
import at.jku.faw.neo4jdemo.model.neo4j.PokeathlonStats;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonFormGenerationsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonFormPokeathlonStatsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonFormRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.FormPokeathlonStatsRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.GameIndexRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.PokeathlonStatsRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.PokemonFormRepository;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PokemonFormService implements IPokemonDataLoader {

    private final CsvPokemonFormRepositoryImpl csvPokemonFormRepository;
    private final PokemonFormRepository pokemonFormRepository;
    private final CsvPokemonFormPokeathlonStatsRepositoryImpl csvPokemonFormPokeathlonStatsRepositoryImpl;
    private final FormPokeathlonStatsRepository formPokeathlonStatsRepository;
    private final PokeathlonStatsRepository pokeathlonStatsRepository;
    private final CsvPokemonFormGenerationsRepositoryImpl csvPokemonFormGenerationsRepositoryImpl;
    private final GameIndexRepository gameIndexRepository;

    public PokemonFormService(CsvPokemonFormRepositoryImpl csvPokemonFormRepository,
                              CsvPokemonFormPokeathlonStatsRepositoryImpl csvPokemonFormPokeathlonStatsRepositoryImpl,
                              FormPokeathlonStatsRepository formPokeathlonStatsRepository,
                              PokeathlonStatsRepository pokeathlonStatsRepository,
                              CsvPokemonFormGenerationsRepositoryImpl csvPokemonFormGenerationsRepositoryImpl,
                              PokemonFormRepository pokemonFormRepository, GameIndexRepository gameIndexRepository) {
        this.csvPokemonFormRepository = csvPokemonFormRepository;
        this.pokemonFormRepository = pokemonFormRepository;
        this.csvPokemonFormPokeathlonStatsRepositoryImpl = csvPokemonFormPokeathlonStatsRepositoryImpl;
        this.formPokeathlonStatsRepository = formPokeathlonStatsRepository;
        this.pokeathlonStatsRepository = pokeathlonStatsRepository;
        this.csvPokemonFormGenerationsRepositoryImpl = csvPokemonFormGenerationsRepositoryImpl;
        this.gameIndexRepository = gameIndexRepository;
    }

    @Override
    public String getEntityName() { return "PokemonForm"; }

    @Override
    @Transactional
    public void loadNodes() {
		for (CsvPokemonForm csv : csvPokemonFormRepository.getAll()) {
			pokemonFormRepository.insertPokemonForm(csv.getId(), csv.getIdentifier(), csv.getIsDefault() != 0, csv.getIsMega() != 0,
					csv.getIsBattleOnly() != 0);
		}
	}

    @Override
    @Transactional
    public void loadRelationships() {
        csvPokemonFormRepository.getAll().forEach(csvPokemonForm -> {
            if (csvPokemonForm.getPokemonId() != null) {
                pokemonFormRepository.linkPokemonFormToPokemon(csvPokemonForm.getId(), csvPokemonForm.getPokemonId());
            }
            csvPokemonFormGenerationsRepositoryImpl.getByPokemonFormId(csvPokemonForm.getId()).forEach(
                    csvPokemonFormGenerations ->
                        gameIndexRepository.linkPokemonFormHasGameIndex(csvPokemonForm.getId(), csvPokemonFormGenerations.getGenerationId(), csvPokemonFormGenerations.getGameIndex())
                        );

            csvPokemonFormPokeathlonStatsRepositoryImpl.getAll().stream()
                .filter(stats -> Objects.equals(stats.getPokemonFormId(), csvPokemonForm.getId()))
                .forEach(stats -> {
                    FormPokeathlonStats formPokeathlonStats = formPokeathlonStatsRepository.insertFormPokeathlonStats(stats.getMinimumStat(),
                            stats.getBaseStat(), stats.getMaximumStat());
                    Optional<PokeathlonStats> pokeStats = pokeathlonStatsRepository.findById(stats.getPokeathlonStatId());
                    pokeStats.ifPresent(pokeathlonStats -> {
                        formPokeathlonStats.setPokeathlonStats(pokeathlonStats);
                        formPokeathlonStatsRepository.save(formPokeathlonStats);
                        pokemonFormRepository.linkPokemonFormToPokeathlonStats(csvPokemonForm.getId(), pokeathlonStats.getId(), formPokeathlonStats.getMinimumStat(), formPokeathlonStats.getBaseStat(), formPokeathlonStats.getMaximumStat());
                    });
                });
        });
    }
}
