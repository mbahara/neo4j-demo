package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.Stats;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StatsRepository extends Neo4jRepository<Stats, Long> {
    Optional<Stats> findByIdentifier(String identifier);

    
    @Query("""
    UNWIND $rows AS row
    MERGE (n:Stats {id: row.id})
    SET n.identifier = row.identifier,
        n.isBattleOnly = row.isBattleOnly,
        n.gameIndex = row.gameIndex
    RETURN count(n)
    """)
    Integer batchInsertStats(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:Stats {id: $id})
        ON CREATE SET n.identifier = $identifier, n.isBattleOnly = $isBattleOnly, n.gameIndex = $gameIndex
        ON MATCH  SET n.identifier = $identifier, n.isBattleOnly = $isBattleOnly, n.gameIndex = $gameIndex
        RETURN n
        """)
    Stats insertStats(@Param("id") Long id, @Param("identifier") String identifier, @Param("isBattleOnly") boolean isBattleOnly, @Param("gameIndex") int gameIndex);


    @Query("""
        MATCH (s:Stats {id: $statsId})
        MATCH (t:DamageClass {id: $damageClassId})
        MERGE (s)-[:HAS_DAMAGE_CLASS]->(t)
        """)
    void linkStatsToDamageClass(@Param("statsId") Long statsId,
                        @Param("damageClassId") Long damageClassId);
}
