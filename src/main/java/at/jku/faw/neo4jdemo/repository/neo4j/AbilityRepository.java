package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.Ability;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AbilityRepository extends Neo4jRepository<Ability, Long> {
    Optional<Ability> findByName(String name);

    @Query("""
    UNWIND $rows AS row
    MERGE (n:Ability {id: row.id})
    SET n.name = row.name,
        n.isMainSeries = row.isMainSeries,
        n.shortEffect = row.shortEffect,
        n.effect = row.effect
    RETURN count(n)
    """)
    Integer batchInsertAbilities(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:Ability {id: $id})
        ON CREATE SET n.name = $name, n.isMainSeries = $isMainSeries, n.shortEffect = $shortEffect, n.effect = $effect
        ON MATCH  SET n.name = $name, n.isMainSeries = $isMainSeries, n.shortEffect = $shortEffect, n.effect = $effect
        RETURN n
        """)
    Ability insertAbility(@Param("id") Long id, @Param("name") String name, @Param("isMainSeries") boolean isMainSeries, @Param("shortEffect") String shortEffect, @Param("effect") String effect);


    @Query("""
        MATCH (s:Ability {id: $abilityId})
        MATCH (t:Generation {id: $generationId})
        MERGE (s)-[:INTRODUCED_IN]->(t)
        """)
    void linkAbilityToGeneration(@Param("abilityId") Long abilityId,
                        @Param("generationId") Long generationId);

    @Query("""
        MATCH (s:Ability {id: $abilityId})
        MATCH (t:ChangeEvent {id: $changeEventId})
        MERGE (s)-[:HAS_CHANGELOG]->(t)
        """)
    void linkAbilityToChangeEvent(@Param("abilityId") Long abilityId,
                        @Param("changeEventId") Long changeEventId);
}
