package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.Effect;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface EffectRepository extends Neo4jRepository<Effect, Long> {
	@Query("""
        MATCH (s:Move {id: $moveId})
        MATCH (t:MoveEffect {id: $moveEffectId})
        MERGE (s)-[:HAS_EFFECT {effectChance: $effectChance}]->(t)
        """)
	void linkMoveToMoveEffect(@Param("moveId") Long moveId,
							  @Param("moveEffectId") Long moveEffectId, @Param("effectChance") int effectChance);
}
