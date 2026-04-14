package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.Version;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VersionRepository extends Neo4jRepository<Version, Long> {
    Optional<Version> findByIdentifier(String identifier);
    Optional<Version> findByName(String name);

    @Query("""
    UNWIND $rows AS row
    MERGE (n:Version {id: row.id})
    SET n.identifier = row.identifier,
        n.name = row.name
    """)
    void batchInsertVersions(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:Version {id: $id})
        ON CREATE SET n.identifier = $identifier, n.name = $name
        ON MATCH  SET n.identifier = $identifier, n.name = $name
        RETURN n
        """)
    Version insertVersion(@Param("id") Long id, @Param("identifier") String identifier, @Param("name") String name);


    @Query("""
        MATCH (s:Version {id: $versionId})
        MATCH (t:VersionGroup {id: $versionGroupId})
        MERGE (s)-[:PART_OF_GROUP]->(t)
        """)
    void linkVersionToVersionGroup(@Param("versionId") Long versionId,
                        @Param("versionGroupId") Long versionGroupId);
}
