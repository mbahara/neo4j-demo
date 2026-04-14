package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.Region;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends Neo4jRepository<Region, Long> {
    Optional<Region> findByIdentifier(String identifier);

    
    @Query("""
    UNWIND $rows AS row
    MERGE (n:Region {id: row.id})
    SET n.identifier = row.identifier
    """)
    void batchInsertRegions(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:Region {id: $id})
        ON CREATE SET n.identifier = $identifier
        ON MATCH  SET n.identifier = $identifier
        RETURN n
        """)
    Region insertRegion(@Param("id") Long id, @Param("identifier") String identifier);
}
