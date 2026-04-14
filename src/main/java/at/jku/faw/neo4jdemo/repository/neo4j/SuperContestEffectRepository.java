package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.SuperContestEffect;
import java.util.List;
import java.util.Map;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SuperContestEffectRepository extends Neo4jRepository<SuperContestEffect, Long> {

    
    @Query("""
    UNWIND $rows AS row
    MERGE (n:SuperContestEffect {id: row.id})
    SET n.appeal = row.appeal,
        n.flavorText = row.flavorText
    """)
    void batchInsertSuperContestEffects(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:SuperContestEffect {id: $id})
        ON CREATE SET n.appeal = $appeal, n.flavorText = $flavorText
        ON MATCH  SET n.appeal = $appeal, n.flavorText = $flavorText
        RETURN n
        """)
    SuperContestEffect insertSuperContestEffect(@Param("id") Long id, @Param("appeal") int appeal, @Param("flavorText") String flavorText);


    @Query("""
        MATCH (s:SuperContestEffect {id: $superContestEffectId})
        MATCH (t:Move {id: $moveId})
        MERGE (t)-[:HAS_SUPER_CONTEST_EFFECT]->(s)
        """)
    void linkSuperContestEffectToMove(@Param("superContestEffectId") Long superContestEffectId,
                        @Param("moveId") Long moveId);
}
