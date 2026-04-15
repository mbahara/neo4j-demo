package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.ContestEffect;
import java.util.List;
import java.util.Map;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestEffectRepository extends Neo4jRepository<ContestEffect, Long> {

    
    @Query("""
    UNWIND $rows AS row
    MERGE (n:ContestEffect {id: row.id})
    SET n.appeal = row.appeal,
        n.jam = row.jam,
        n.flavorText = row.flavorText,
        n.effect = row.effect
    RETURN count(n)
    """)
    Integer batchInsertContestEffects(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:ContestEffect {id: $id})
        ON CREATE SET n.appeal = $appeal, n.jam = $jam, n.flavorText = $flavorText, n.effect = $effect
        ON MATCH  SET n.appeal = $appeal, n.jam = $jam, n.flavorText = $flavorText, n.effect = $effect
        RETURN n
        """)
    ContestEffect insertContestEffect(@Param("id") Long id, @Param("appeal") int appeal, @Param("jam") int jam, @Param("flavorText") String flavorText, @Param("effect") String effect);
}
