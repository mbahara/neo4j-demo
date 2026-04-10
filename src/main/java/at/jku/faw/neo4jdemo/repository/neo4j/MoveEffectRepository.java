package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.MoveEffect;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MoveEffectRepository extends Neo4jRepository<MoveEffect, Long> {

    @Query("""
        MERGE (n:MoveEffect {id: $id})
        ON CREATE SET n.shortEffect = $shortEffect, n.effect = $effect
        ON MATCH  SET n.shortEffect = $shortEffect, n.effect = $effect
        RETURN n
        """)
    MoveEffect insertMoveEffect(@Param("id") Long id, @Param("shortEffect") int shortEffect, @Param("effect") String effect);
}
