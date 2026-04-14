package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.VersionGroup;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VersionGroupRepository extends Neo4jRepository<VersionGroup, Long> {
    Optional<VersionGroup> findByIdentifier(String identifier);

    
    @Query("""
    UNWIND $rows AS row
    MERGE (n:VersionGroup {id: row.id})
    SET n.identifier = row.identifier,
        n.order = row.order
    """)
    void batchInsertVersionGroups(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:VersionGroup {id: $id})
        ON CREATE SET n.identifier = $identifier, n.order = $order
        ON MATCH  SET n.identifier = $identifier, n.order = $order
        RETURN n
        """)
    VersionGroup insertVersionGroup(@Param("id") Long id, @Param("identifier") String identifier, @Param("order") int order);


    @Query("""
        MATCH (s:VersionGroup {id: $versionGroupId})
        MATCH (t:Generation {id: $generationId})
        MERGE (s)-[:INTRODUCED_IN]->(t)
        """)
    void linkVersionGroupToGeneration(@Param("versionGroupId") Long versionGroupId,
                        @Param("generationId") Long generationId);

    @Query("""
        MATCH (s:VersionGroup {id: $versionGroupId})
        MATCH (t:Version {id: $versionId})
        MERGE (s)<-[:PART_OF_GROUP]-(t)
        """)
    void linkVersionGroupToVersion(@Param("versionGroupId") Long versionGroupId,
                        @Param("versionId") Long versionId);

    @Query("""
        MATCH (s:VersionGroup {id: $versionGroupId})
        MATCH (t:Region {id: $regionId})
        MERGE (s)-[:CONTAINS_REGION]->(t)
        """)
    void linkVersionGroupToRegion(@Param("versionGroupId") Long versionGroupId,
                        @Param("regionId") Long regionId);

    @Query("""
        MATCH (s:VersionGroup {id: $versionGroupId})
        MATCH (t:MoveMethod {id: $moveMethodId})
        MERGE (s)-[:ALLOWS_MOVE_METHOD]->(t)
        """)
    void linkVersionGroupToMoveMethod(@Param("versionGroupId") Long versionGroupId,
                        @Param("moveMethodId") Long moveMethodId);
}
