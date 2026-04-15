package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvPokemonFormGenerations;
import at.jku.faw.neo4jdemo.model.csv.CsvPokemonFormPokeathlonStats;
import at.jku.faw.neo4jdemo.model.neo4j.FormPokeathlonStats;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonFormGenerationsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonFormPokeathlonStatsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvPokemonFormRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.FormPokeathlonStatsRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.GameIndexRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.PokemonFormRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PokemonFormService implements IPokemonDataLoader {

    private final CsvPokemonFormRepositoryImpl csvPokemonFormRepository;
    private final PokemonFormRepository pokemonFormRepository;
    private final CsvPokemonFormPokeathlonStatsRepositoryImpl csvPokemonFormPokeathlonStatsRepositoryImpl;
    private final FormPokeathlonStatsRepository formPokeathlonStatsRepository;
    private final CsvPokemonFormGenerationsRepositoryImpl csvPokemonFormGenerationsRepositoryImpl;
    private final GameIndexRepository gameIndexRepository;

    public PokemonFormService(CsvPokemonFormRepositoryImpl csvPokemonFormRepository,
                              CsvPokemonFormPokeathlonStatsRepositoryImpl csvPokemonFormPokeathlonStatsRepositoryImpl,
                              FormPokeathlonStatsRepository formPokeathlonStatsRepository,
                              CsvPokemonFormGenerationsRepositoryImpl csvPokemonFormGenerationsRepositoryImpl,
                              PokemonFormRepository pokemonFormRepository, GameIndexRepository gameIndexRepository) {
        this.csvPokemonFormRepository = csvPokemonFormRepository;
        this.pokemonFormRepository = pokemonFormRepository;
        this.csvPokemonFormPokeathlonStatsRepositoryImpl = csvPokemonFormPokeathlonStatsRepositoryImpl;
        this.formPokeathlonStatsRepository = formPokeathlonStatsRepository;
        this.csvPokemonFormGenerationsRepositoryImpl = csvPokemonFormGenerationsRepositoryImpl;
        this.gameIndexRepository = gameIndexRepository;
    }

    @Override
    public String getEntityName() { return "PokemonForm"; }

    @Override
    @Transactional
    public void loadNodes() {
        List<Map<String, Object>> rows = csvPokemonFormRepository.getAll().stream()
                .map(csvPokemonForm -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csvPokemonForm.getId());
                    row.put("identifier", csvPokemonForm.getIdentifier());
                    row.put("isDefault", csvPokemonForm.getIsDefault());
                    row.put("isMega", csvPokemonForm.getIsMega());
                    row.put("isBattleOnly", csvPokemonForm.getIsBattleOnly());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            pokemonFormRepository.batchInsertPokemonForms(rows);
        }
	}

    @Override
    @Transactional
    public void loadRelationships() {
        Map<Long, List<CsvPokemonFormGenerations>> generationMap =
                csvPokemonFormGenerationsRepositoryImpl.getAll().stream()
                        .collect(Collectors.groupingBy(CsvPokemonFormGenerations::getPokemonFormId));

        Map<Long, List<CsvPokemonFormPokeathlonStats>> pokeathlonCsvMap =
                csvPokemonFormPokeathlonStatsRepositoryImpl.getAll().stream()
                        .collect(Collectors.groupingBy(CsvPokemonFormPokeathlonStats::getPokemonFormId));

        csvPokemonFormRepository.getAll().forEach(csv -> {
            Long formId = csv.getId();

            if (csv.getPokemonId() != null) {
                pokemonFormRepository.linkPokemonFormToPokemon(formId, csv.getPokemonId());
            }

            // Generation
            List<CsvPokemonFormGenerations> gens = generationMap.get(formId);
            if (gens != null) {
                gens.forEach(g -> gameIndexRepository.linkPokemonFormHasGameIndex(
                        formId, g.getGenerationId(), g.getGameIndex()));
            }

            // Pokeathlon Stats
            List<CsvPokemonFormPokeathlonStats> statsList = pokeathlonCsvMap.get(formId);
            if (statsList != null) {
                statsList.forEach(stats -> {
                    FormPokeathlonStats formPokeathlonStats = formPokeathlonStatsRepository.insertFormPokeathlonStats(
                            stats.getMinimumStat(), stats.getBaseStat(), stats.getMaximumStat());

                    pokemonFormRepository.linkPokemonFormToPokeathlonStats(
                            formId,
                            formPokeathlonStats.getId(),
                            formPokeathlonStats.getMinimumStat(),
                            formPokeathlonStats.getBaseStat(),
                            formPokeathlonStats.getMaximumStat()
                    );
                });
            }
        });
    }
}
