package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.FlingEffect;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FlingEffectRepository extends Neo4jRepository<FlingEffect, Long> {
    Optional<FlingEffect> findByIdentifier(String identifier);

    
    @Query("""
    UNWIND $rows AS row
    MERGE (n:FlingEffect {id: row.id})
    SET n.identifier = row.identifier,
        n.effect = row.effect
    RETURN count(n)
    """)
    Integer batchInsertFlingEffects(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:FlingEffect {id: $id})
        ON CREATE SET n.identifier = $identifier, n.effect = $effect
        ON MATCH  SET n.identifier = $identifier, n.effect = $effect
        RETURN n
        """)
    FlingEffect insertFlingEffect(@Param("id") Long id, @Param("identifier") String identifier, @Param("effect") String effect);

    @Query("CREATE INDEX flingeffect_id_idx IF NOT EXISTS FOR (n:FlingEffect) ON (n.id)")
    void createIdIndex();
}
