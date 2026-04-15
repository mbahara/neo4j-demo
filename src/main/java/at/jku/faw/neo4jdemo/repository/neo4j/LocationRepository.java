package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.Location;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends Neo4jRepository<Location, Long> {
    Optional<Location> findByIdentifier(String identifier);
    Optional<Location> findByName(String name);

    
    @Query("""
    UNWIND $rows AS row
    MERGE (n:Location {id: row.id})
    SET n.identifier = row.identifier,
        n.name = row.name,
        n.subtitle = row.subtitle
    RETURN count(n)
    """)
    Integer batchInsertLocations(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:Location {id: $id})
        ON CREATE SET n.identifier = $identifier, n.name = $name, n.subtitle = $subtitle
        ON MATCH  SET n.identifier = $identifier, n.name = $name, n.subtitle = $subtitle
        RETURN n
        """)
    Location insertLocation(@Param("id") Long id, @Param("identifier") String identifier, @Param("name") String name, @Param("subtitle") String subtitle);


    @Query("""
        MATCH (s:Location {id: $locationId})
        MATCH (t:Region {id: $regionId})
        MERGE (s)-[:IN_REGION]->(t)
        """)
    void linkLocationToRegion(@Param("locationId") Long locationId,
                        @Param("regionId") Long regionId);
}
