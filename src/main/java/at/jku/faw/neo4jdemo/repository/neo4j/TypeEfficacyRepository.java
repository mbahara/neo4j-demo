package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.TypeEfficacy;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface TypeEfficacyRepository extends Neo4jRepository<TypeEfficacy, Long> {
	@Query("""
        MATCH (s:Type {id: $firstTypeId})
        MATCH (t:Type {id: $secondTypeId})
        MERGE (s)-[:EFFICACY_AGAINST {damageFactor: $damageFactor}]->(t)
        """)
	void linkTypeToType(@Param("firstTypeId") Long firstTypeId, @Param("secondTypeId") Long secondTypeId, @Param("damageFactor") int damageFactor);
}
