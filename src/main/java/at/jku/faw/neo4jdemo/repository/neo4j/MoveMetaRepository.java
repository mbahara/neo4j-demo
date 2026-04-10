package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.MoveMeta;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MoveMetaRepository extends Neo4jRepository<MoveMeta, Long> {

    @Query("""
        MERGE (n:MoveMeta {id: $id})
        ON CREATE SET n.minHits = $minHits, n.maxHits = $maxHits, n.minTurns = $minTurns, n.maxTurns = $maxTurns, n.drain = $drain, n.healing = $healing, n.critRate = $critRate, n.ailmentChance = $ailmentChance, n.flinchChance = $flinchChance, n.statChance = $statChance
        ON MATCH  SET n.minHits = $minHits, n.maxHits = $maxHits, n.minTurns = $minTurns, n.maxTurns = $maxTurns, n.drain = $drain, n.healing = $healing, n.critRate = $critRate, n.ailmentChance = $ailmentChance, n.flinchChance = $flinchChance, n.statChance = $statChance
        RETURN n
        """)
    MoveMeta insertMoveMeta(@Param("id") Long id, @Param("minHits") int minHits, @Param("maxHits") int maxHits, @Param("minTurns") int minTurns, @Param("maxTurns") int maxTurns, @Param("drain") int drain, @Param("healing") int healing, @Param("critRate") int critRate, @Param("ailmentChance") int ailmentChance, @Param("flinchChance") int flinchChance, @Param("statChance") int statChance);


    @Query("""
        MATCH (s:MoveMeta {id: $moveMetaId})
        MATCH (t:MoveAilment {id: $moveAilmentId})
        MERGE (s)-[:CAUSES_AILMENT]->(t)
        """)
    void linkMoveMetaToMoveAilment(@Param("moveMetaId") Long moveMetaId,
                        @Param("moveAilmentId") Long moveAilmentId);

    @Query("""
        MATCH (s:MoveMeta {id: $moveMetaId})
        MATCH (t:MoveCategory {id: $moveCategoryId})
        MERGE (s)-[:IN_META_CATEGORY]->(t)
        """)
    void linkMoveMetaToMoveCategory(@Param("moveMetaId") Long moveMetaId,
                        @Param("moveCategoryId") Long moveCategoryId);
}
