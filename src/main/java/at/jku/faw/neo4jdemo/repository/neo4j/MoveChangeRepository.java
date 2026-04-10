package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.MoveChange;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MoveChangeRepository extends Neo4jRepository<MoveChange, Long> {

    @Query("""
        MERGE (n:MoveChange {id: $id})
        ON CREATE SET n.power = $power, n.pp = $pp, n.accuracy = $accuracy, n.priority = $priority, n.effectChance = $effectChance
        ON MATCH  SET n.power = $power, n.pp = $pp, n.accuracy = $accuracy, n.priority = $priority, n.effectChance = $effectChance
        RETURN n
        """)
    MoveChange insertMoveChange(@Param("id") Long id, @Param("power") int power, @Param("pp") int pp, @Param("accuracy") int accuracy, @Param("priority") int priority, @Param("effectChance") int effectChance);


    @Query("""
        MATCH (s:MoveChange {id: $moveChangeId})
        MATCH (t:VersionGroup {id: $versionGroupId})
        MERGE (s)-[:CHANGED_IN]->(t)
        """)
    void linkMoveChangeToVersionGroup(@Param("moveChangeId") Long moveChangeId,
                        @Param("versionGroupId") Long versionGroupId);

    @Query("""
        MATCH (s:MoveChange {id: $moveChangeId})
        MATCH (t:Type {id: $typeId})
        MERGE (s)-[:FOR_TYPE]->(t)
        """)
    void linkMoveChangeToType(@Param("moveChangeId") Long moveChangeId,
                        @Param("typeId") Long typeId);

    @Query("""
        MATCH (s:MoveChange {id: $moveChangeId})
        MATCH (t:MoveEffect {id: $moveEffectId})
        MERGE (s)-[:HAS_EFFECT]->(t)
        """)
    void linkMoveChangeToMoveEffect(@Param("moveChangeId") Long moveChangeId,
                        @Param("moveEffectId") Long moveEffectId);

    @Query("""
        MATCH (s:MoveChange {id: $moveChangeId})
        MATCH (t:MoveTarget {id: $moveTargetId})
        MERGE (s)-[:TARGETS]->(t)
        """)
    void linkMoveChangeToMoveTarget(@Param("moveChangeId") Long moveChangeId,
                        @Param("moveTargetId") Long moveTargetId);
}
