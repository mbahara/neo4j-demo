package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.EvolutionStep;
import java.util.List;
import java.util.Map;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EvolutionStepRepository extends Neo4jRepository<EvolutionStep, Long> {

    
    @Query("""
    UNWIND $rows AS row
    MERGE (n:EvolutionStep {id: row.id})
    SET n.minimumLevel = row.minimumLevel,
        n.minimumHappiness = row.minimumHappiness,
        n.timeOfDay = row.timeOfDay,
        n.relativePhysicalStats = row.relativePhysicalStats,
        n.needsOverworldRain = row.needsOverworldRain,
        n.turnUpsideDown = row.turnUpsideDown,
        n.minimumBeauty = row.minimumBeauty,
        n.minimumAffection = row.minimumAffection
    RETURN count(n)
    """)
    Integer batchInsertEvolutionSteps(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:EvolutionStep {id: $id})
        ON CREATE SET n.minimumLevel = $minimumLevel, n.minimumHappiness = $minimumHappiness, n.timeOfDay = $timeOfDay, n.relativePhysicalStats = $relativePhysicalStats, n.needsOverworldRain = $needsOverworldRain, n.turnUpsideDown = $turnUpsideDown, n.minimumBeauty = $minimumBeauty, n.minimumAffection = $minimumAffection
        ON MATCH  SET n.minimumLevel = $minimumLevel, n.minimumHappiness = $minimumHappiness, n.timeOfDay = $timeOfDay, n.relativePhysicalStats = $relativePhysicalStats, n.needsOverworldRain = $needsOverworldRain, n.turnUpsideDown = $turnUpsideDown, n.minimumBeauty = $minimumBeauty, n.minimumAffection = $minimumAffection
        RETURN n
        """)
    EvolutionStep insertEvolutionStep(@Param("id") Long id, @Param("minimumLevel") int minimumLevel, @Param("minimumHappiness") int minimumHappiness, @Param("timeOfDay") String timeOfDay, @Param("relativePhysicalStats") int relativePhysicalStats, @Param("needsOverworldRain") boolean needsOverworldRain, @Param("turnUpsideDown") int turnUpsideDown, @Param("minimumBeauty") int minimumBeauty, @Param("minimumAffection") int minimumAffection);


    @Query("""
        MATCH (s:EvolutionStep {id: $evolutionStepId})
        MATCH (t:EvolutionTrigger {id: $evolutionTriggerId})
        MERGE (s)-[:TRIGGERED_BY]->(t)
        """)
    void linkEvolutionStepToEvolutionTrigger(@Param("evolutionStepId") Long evolutionStepId,
                        @Param("evolutionTriggerId") Long evolutionTriggerId);

    @Query("""
        MATCH (s:EvolutionStep {id: $evolutionStepId})
        MATCH (t:Item {id: $itemId})
        MERGE (s)-[:REQUIRES]->(t)
        """)
    void linkEvolutionStepToTriggerItem(@Param("evolutionStepId") Long evolutionStepId,
                        @Param("itemId") Long itemId);

    @Query("""
        MATCH (s:EvolutionStep {id: $evolutionStepId})
        MATCH (t:Gender {id: $genderId})
        MERGE (s)-[:HAS_GENDER]->(t)
        """)
    void linkEvolutionStepToGender(@Param("evolutionStepId") Long evolutionStepId,
                        @Param("genderId") Long genderId);

    @Query("""
        MATCH (s:EvolutionStep {id: $evolutionStepId})
        MATCH (t:Item {id: $itemId})
        MERGE (s)-[:HELD_ITEM]->(t)
        """)
    void linkEvolutionStepToItem(@Param("evolutionStepId") Long evolutionStepId,
                        @Param("itemId") Long itemId);

    @Query("""
        MATCH (s:EvolutionStep {id: $evolutionStepId})
        MATCH (t:Move {id: $moveId})
        MERGE (s)-[:KNOWN_MOVE]->(t)
        """)
    void linkEvolutionStepToMove(@Param("evolutionStepId") Long evolutionStepId,
                        @Param("moveId") Long moveId);

    @Query("""
        MATCH (s:EvolutionStep {id: $evolutionStepId})
        MATCH (t:Location {id: $locationId})
        MERGE (s)-[:AT_LOCATION]->(t)
        """)
    void linkEvolutionStepToLocation(@Param("evolutionStepId") Long evolutionStepId,
                        @Param("locationId") Long locationId);

    @Query("""
        MATCH (s:EvolutionStep {id: $evolutionStepId})
        MATCH (t:PokemonSpecies {id: $pokemonSpeciesId})
        MERGE (s)-[:RESULTS_IN]->(t)
        """)
    void linkEvolutionStepToPokemonSpecies(@Param("evolutionStepId") Long evolutionStepId,
                        @Param("pokemonSpeciesId") Long pokemonSpeciesId);

    @Query("CREATE INDEX evolutionstep_id_idx IF NOT EXISTS FOR (n:EvolutionStep) ON (n.id)")
    void createIdIndex();
}
