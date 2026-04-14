package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.PokeathlonStatsModifier;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface PokeathlonStatsModifierRepository extends Neo4jRepository<PokeathlonStatsModifier, Long> {
	@Query("""
        MATCH (s:Nature {id: $natureId})
        MATCH (t:PokeathlonStats {id: $pokeathlonStatsId})
        MERGE (s)-[:POKEATHLON_MODIFIER {maxChange: $maxChange}]->(t)
        """)
	void linkNatureToPokeathlonStats(@Param("natureId") Long natureId, @Param("pokeathlonStatsId") Long pokeathlonStatsId, @Param("maxChange") int maxChange);
}
