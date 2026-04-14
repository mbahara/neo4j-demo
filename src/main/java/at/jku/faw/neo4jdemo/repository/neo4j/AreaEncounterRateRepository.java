package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.AreaEncounterRate;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface AreaEncounterRateRepository extends Neo4jRepository<AreaEncounterRate, Long> {
	@Query("""
        MATCH (s:Area {id: $areaId})
        MATCH (t:EncounterMethod {id: $encounterMethodId})
        MERGE (s)-[:HAS_ENCOUNTER_RATE {rate: $rate, versionId: $versionId}]->(t)
        """)
	void linkAreaToEncounterMethod(@Param("areaId") Long areaId, @Param("encounterMethodId") Long encounterMethodId, @Param("rate") int rate, @Param("versionId") Long versionId);
}
