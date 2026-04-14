package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.PokemonSpecies;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PokemonSpeciesRepository extends Neo4jRepository<PokemonSpecies, Long> {
    Optional<PokemonSpecies> findByName(String name);

    @Query("""
        MERGE (n:PokemonSpecies {id: $id})
        ON CREATE SET n.name = $name, n.genus = $genus, n.genderRate = $genderRate, n.captureRate = $captureRate, n.baseHappiness = $baseHappiness, n.isBaby = $isBaby, n.isLegendary = $isLegendary, n.isMythical = $isMythical, n.hatchCounter = $hatchCounter, n.hasGenderDifferences = $hasGenderDifferences, n.formsSwitchable = $formsSwitchable, n.order = $order, n.conquestOrder = $conquestOrder
        ON MATCH  SET n.name = $name, n.genus = $genus, n.genderRate = $genderRate, n.captureRate = $captureRate, n.baseHappiness = $baseHappiness, n.isBaby = $isBaby, n.isLegendary = $isLegendary, n.isMythical = $isMythical, n.hatchCounter = $hatchCounter, n.hasGenderDifferences = $hasGenderDifferences, n.formsSwitchable = $formsSwitchable, n.order = $order, n.conquestOrder = $conquestOrder
        RETURN n
        """)
    PokemonSpecies insertPokemonSpecies(@Param("id") Long id, @Param("name") String name, @Param("genus") String genus, @Param("genderRate") int genderRate, @Param("captureRate") int captureRate, @Param("baseHappiness") int baseHappiness, @Param("isBaby") boolean isBaby, @Param("isLegendary") boolean isLegendary, @Param("isMythical") boolean isMythical, @Param("hatchCounter") int hatchCounter, @Param("hasGenderDifferences") boolean hasGenderDifferences, @Param("formsSwitchable") boolean formsSwitchable, @Param("order") int order, @Param("conquestOrder") int conquestOrder);


    @Query("""
        MATCH (s:PokemonSpecies {id: $pokemonSpeciesId})
        MATCH (t:EvolutionStep {id: $evolutionStepId})
        MERGE (s)-[:HAS_EVOLUTION_STEP]->(t)
        """)
    void linkPokemonSpeciesToEvolutionStep(@Param("pokemonSpeciesId") Long pokemonSpeciesId,
                        @Param("evolutionStepId") Long evolutionStepId);

    @Query("""
        MATCH (s:PokemonSpecies {id: $pokemonSpeciesId})
        MATCH (t:EvolutionChain {id: $evolutionChainId})
        MERGE (s)-[:PART_OF_CHAIN]->(t)
        """)
    void linkPokemonSpeciesIsPartOfEvolutionChain(@Param("pokemonSpeciesId") Long pokemonSpeciesId,
                        @Param("evolutionChainId") Long evolutionChainId);

    @Query("""
        MATCH (current:PokemonSpecies {id: $pokemonSpeciesId})
        MATCH (base:PokemonSpecies {id: $evolvesFromId})
        MERGE (base)-[:EVOLVES_FROM]->(current)
        """)
    void linkPokemonSpeciesToPokemonSpecies(@Param("pokemonSpeciesId") Long pokemonSpeciesId,
                        @Param("evolvesFromId") Long evolvesFromId);

    @Query("""
        MATCH (s:PokemonSpecies {id: $pokemonSpeciesId})
        MATCH (t:PokemonColor {id: $pokemonColorId})
        MERGE (s)-[:HAS_COLOR]->(t)
        """)
    void linkPokemonSpeciesToPokemonColor(@Param("pokemonSpeciesId") Long pokemonSpeciesId,
                        @Param("pokemonColorId") Long pokemonColorId);

    @Query("""
        MATCH (s:PokemonSpecies {id: $pokemonSpeciesId})
        MATCH (t:Generation {id: $generationId})
        MERGE (s)-[:BELONGS_TO_GEN]->(t)
        """)
    void linkPokemonSpeciesToGeneration(@Param("pokemonSpeciesId") Long pokemonSpeciesId,
                        @Param("generationId") Long generationId);

    @Query("""
        MATCH (s:PokemonSpecies {id: $pokemonSpeciesId})
        MATCH (t:PokemonShape {id: $pokemonShapeId})
        MERGE (s)-[:HAS_SHAPE]->(t)
        """)
    void linkPokemonSpeciesToPokemonShape(@Param("pokemonSpeciesId") Long pokemonSpeciesId,
                        @Param("pokemonShapeId") Long pokemonShapeId);

    @Query("""
        MATCH (s:PokemonSpecies {id: $pokemonSpeciesId})
        MATCH (t:PokemonHabitat {id: $pokemonHabitatId})
        MERGE (s)-[:LIVES_IN]->(t)
        """)
    void linkPokemonSpeciesToPokemonHabitat(@Param("pokemonSpeciesId") Long pokemonSpeciesId,
                        @Param("pokemonHabitatId") Long pokemonHabitatId);

    @Query("""
        MATCH (s:PokemonSpecies {id: $pokemonSpeciesId})
        MATCH (t:GrowthRate {id: $growthRateId})
        MERGE (s)-[:HAS_GROWTH_RATE]->(t)
        """)
    void linkPokemonSpeciesToGrowthRate(@Param("pokemonSpeciesId") Long pokemonSpeciesId,
                        @Param("growthRateId") Long growthRateId);

    @Query("""
        MATCH (s:PokemonSpecies {id: $pokemonSpeciesId})
        MATCH (t:EggGroup {id: $eggGroupId})
        MERGE (s)-[:BELONGS_TO_EGG_GROUP]->(t)
        """)
    void linkPokemonSpeciesToEggGroup(@Param("pokemonSpeciesId") Long pokemonSpeciesId,
                        @Param("eggGroupId") Long eggGroupId);
}
