package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.Highlights;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface HighlightsRepository extends Neo4jRepository<Highlights, Long> {
	@Query("""
        MATCH (s:Characteristic {id: $characteristicId})
        MATCH (t:Stat {id: $statId})
        MERGE (s)-[:HIGHLIGHTS {geneMod5: $geneMod5}]->(t)
        """)
	void linkCharacteristicToStat(@Param("characteristicId") Long characteristicId, @Param("statId") Long statId, @Param("geneMod5") int geneMod5);
}
