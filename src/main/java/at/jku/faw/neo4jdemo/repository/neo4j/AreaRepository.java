package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.Area;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaRepository extends Neo4jRepository<Area, Long> {
    Optional<Area> findByIdentifier(String identifier);
    Optional<Area> findByName(String name);

    @Query("""
        MERGE (n:Area {id: $id})
        ON CREATE SET n.identifier = $identifier, n.name = $name
        ON MATCH  SET n.identifier = $identifier, n.name = $name
        RETURN n
        """)
    Area insertArea(@Param("id") Long id, @Param("identifier") String identifier, @Param("name") String name);


    @Query("""
        MATCH (s:Area {id: $areaId})
        MATCH (t:Location {id: $locationId})
        MERGE (s)-[:PART_OF]->(t)
        """)
    void linkAreaToLocation(@Param("areaId") Long areaId,
                        @Param("locationId") Long locationId);

}
