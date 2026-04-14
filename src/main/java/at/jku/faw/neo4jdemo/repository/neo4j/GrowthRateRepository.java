package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.GrowthRate;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GrowthRateRepository extends Neo4jRepository<GrowthRate, Long> {
    Optional<GrowthRate> findByIdentifier(String identifier);

    @Query("""
        MERGE (n:GrowthRate {id: $id})
        ON CREATE SET n.identifier = $identifier, n.formula = $formula
        ON MATCH  SET n.identifier = $identifier, n.formula = $formula
        RETURN n
        """)
    GrowthRate insertGrowthRate(@Param("id") Long id, @Param("identifier") String identifier, @Param("formula") String formula);
}
