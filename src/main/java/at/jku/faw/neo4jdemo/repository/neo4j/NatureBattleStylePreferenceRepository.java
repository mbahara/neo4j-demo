package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.NatureBattleStylePreference;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface NatureBattleStylePreferenceRepository extends Neo4jRepository<NatureBattleStylePreference, Long> {
	@Query("""
        MATCH (s:Nature {id: $natureId})
        MATCH (t:MoveBattleStyle {id: $moveBattleStyleId})
        MERGE (s)-[:PREFERRED_BATTLE_STYLE {lowHpPreference: $lowHpPreference, highHpPreference: $highHpPreference}]->(t)
        """)
	void linkNatureToMoveBattleStyle(@Param("natureId") Long natureId, @Param("moveBattleStyleId") Long moveBattleStyleId, @Param("lowHpPreference") int lowHpPreference, @Param("highHpPreference") int highHpPreference);
}
