package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.Move;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MoveRepository extends Neo4jRepository<Move, Long> {
    Optional<Move> findByName(String name);

    
    @Query("""
    UNWIND $rows AS row
    MERGE (n:Move {id: row.id})
    SET n.name = row.name,
        n.power = row.power,
        n.pp = row.pp,
        n.accuracy = row.accuracy,
        n.priority = row.priority
    RETURN count(n)
    """)
    Integer batchInsertMoves(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:Move {id: $id})
        ON CREATE SET n.name = $name, n.power = $power, n.pp = $pp, n.accuracy = $accuracy, n.priority = $priority
        ON MATCH  SET n.name = $name, n.power = $power, n.pp = $pp, n.accuracy = $accuracy, n.priority = $priority
        RETURN n
        """)
    Move insertMove(@Param("id") Long id, @Param("name") String name, @Param("power") int power, @Param("pp") int pp, @Param("accuracy") int accuracy, @Param("priority") int priority);


    @Query("""
        MATCH (s:Move {id: $moveId})
        MATCH (t:Type {id: $typeId})
        MERGE (s)-[:HAS_TYPE]->(t)
        """)
    void linkMoveToType(@Param("moveId") Long moveId,
                        @Param("typeId") Long typeId);

    @Query("""
        MATCH (s:Move {id: $moveId})
        MATCH (t:Generation {id: $generationId})
        MERGE (s)-[:INTRODUCED_IN]->(t)
        """)
    void linkMoveToGeneration(@Param("moveId") Long moveId,
                        @Param("generationId") Long generationId);

    @Query("""
        MATCH (s:Move {id: $moveId})
        MATCH (t:DamageClass {id: $damageClassId})
        MERGE (s)-[:DAMAGE_CATEGORY]->(t)
        """)
    void linkMoveToDamageClass(@Param("moveId") Long moveId,
                        @Param("damageClassId") Long damageClassId);

    @Query("""
        MATCH (s:Move {id: $moveId})
        MATCH (t:MoveTarget {id: $moveTargetId})
        MERGE (s)-[:TARGETS]->(t)
        """)
    void linkMoveToMoveTarget(@Param("moveId") Long moveId,
                        @Param("moveTargetId") Long moveTargetId);

    @Query("""
        MATCH (s:Move {id: $moveId})
        MATCH (t:MoveFlag {id: $moveFlagId})
        MERGE (s)-[:HAS_FLAG]->(t)
        """)
    void linkMoveToMoveFlag(@Param("moveId") Long moveId,
                        @Param("moveFlagId") Long moveFlagId);

    @Query("""
        MATCH (s:Move {id: $moveId})
        MATCH (t:MoveMeta {id: $moveMetaId})
        MERGE (s)-[:HAS_META]->(t)
        """)
    void linkMoveToMoveMeta(@Param("moveId") Long moveId,
                        @Param("moveMetaId") Long moveMetaId);

    @Query("""
        MATCH (s:Move {id: $moveId})
        MATCH (t:MoveChange {id: $moveChangeId})
        MERGE (s)-[:HAS_HISTORY]->(t)
        """)
    void linkMoveToMoveChange(@Param("moveId") Long moveId,
                        @Param("moveChangeId") Long moveChangeId);

    @Query("""
        MATCH (s:Move {id: $moveId})
        MATCH (t:ContestType {id: $contestTypeId})
        MERGE (s)-[:CONTEST_STYLE]->(t)
        """)
    void linkMoveToContestType(@Param("moveId") Long moveId,
                        @Param("contestTypeId") Long contestTypeId);

    @Query("""
        MATCH (s:Move {id: $moveId})
        MATCH (t:ContestEffect {id: $contestEffectId})
        MERGE (s)-[:HAS_CONTEST_EFFECT]->(t)
        """)
    void linkMoveToContestEffect(@Param("moveId") Long moveId,
                        @Param("contestEffectId") Long contestEffectId);

    @Query("""
        MATCH (s:Move {id: $firstMoveId})
        MATCH (t:Move {id: $secondMoveId})
        MERGE (s)-[:COMBOS_WITH]->(t)
        """)
    void linkMoveToComboMove(@Param("firstMoveId") Long firstMoveId,
                        @Param("secondMoveId") Long secondMoveId);

    @Query("""
        MATCH (s:Move {id: $firstMoveId})
        MATCH (t:Move {id: $secondMoveId})
        MERGE (s)-[:COMBOS_IN_SUPER_CONTEST]->(t)
        """)
    void linkMoveToComboMoveInSuperContest(@Param("firstMoveId") Long firstMoveId,
                                           @Param("secondMoveId") Long secondMoveId);
}
