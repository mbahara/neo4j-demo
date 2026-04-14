package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.LevelRequirement;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface LevelRequirementRepository extends Neo4jRepository<LevelRequirement, Long> {
	@Query("""
        MATCH (s:GrowthRate {id: $growthRateId})
        MATCH (t:Level {id: $levelId})
        MERGE (s)-[:REQUIRES_EXP {experience: $experience}]->(t)
        """)
	void linkGrowthRateToLevel(@Param("growthRateId") Long growthRateId, @Param("levelId") Long levelId, @Param("experience") int experience);
}
