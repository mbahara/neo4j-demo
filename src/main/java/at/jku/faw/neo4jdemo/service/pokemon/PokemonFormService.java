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
			pokemonFormRepository.insertPokemonForm(csv.id(), csv.identifier(), csv.isDefault() != 0, csv.isMega() != 0,
					csv.isBattleOnly() != 0);
		}
	}

    @Override
    @Transactional
    public void loadRelationships() {
        csvPokemonFormRepository.getAll().forEach(csvPokemonForm -> {
            if (csvPokemonForm.pokemonId() != null) {
                pokemonFormRepository.linkPokemonFormToPokemon(csvPokemonForm.id(), csvPokemonForm.pokemonId());
            }
            csvPokemonFormGenerationsRepositoryImpl.getByPokemonFormId(csvPokemonForm.id()).forEach(
                    csvPokemonFormGenerations ->
                        gameIndexRepository.linkPokemonFormHasGameIndex(csvPokemonForm.id(), csvPokemonFormGenerations.generationId(), csvPokemonFormGenerations.gameIndex())
                        );

            csvPokemonFormPokeathlonStatsRepositoryImpl.getAll().stream()
                .filter(stats -> Objects.equals(stats.pokemonFormId(), csvPokemonForm.id()))
                .forEach(stats -> {
                    FormPokeathlonStats formPokeathlonStats = formPokeathlonStatsRepository.insertFormPokeathlonStats(stats.minimumStat(),
                            stats.baseStat(), stats.maximumStat());
                    Optional<PokeathlonStats> pokeStats = pokeathlonStatsRepository.findById(stats.pokeathlonStatId());
                    pokeStats.ifPresent(pokeathlonStats -> {
                        formPokeathlonStats.setPokeathlonStats(pokeathlonStats);
                        formPokeathlonStatsRepository.save(formPokeathlonStats);
                        pokemonFormRepository.linkPokemonFormToPokeathlonStats(csvPokemonForm.id(), pokeathlonStats.getId(), formPokeathlonStats.getMinimumStat(), formPokeathlonStats.getBaseStat(), formPokeathlonStats.getMaximumStat());
                    });
                });
        });
    }
}
