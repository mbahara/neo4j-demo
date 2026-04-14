package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.HasStats;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface HasStatsRepository extends Neo4jRepository<HasStats, Long> {
	@Query("""
        MATCH (s:Pokemon {id: $pokemonId})
        MATCH (t:Stats {id: $statsId})
        MERGE (s)-[:HAS_STAT {baseStat: $baseStat, effort: $effort}]->(t)
        """)
	void linkPokemonToStats(@Param("pokemonId") Long pokemonId, @Param("statsId") Long statsId, @Param("baseStat") int baseStat, @Param("effort") int effort);

}
