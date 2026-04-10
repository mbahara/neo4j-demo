package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.Generation;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GenerationRepository extends Neo4jRepository<Generation, Long> {
    Optional<Generation> findByIdentifier(String identifier);
    Optional<Generation> findByName(String name);

    @Query("""
        MERGE (n:Generation {id: $id})
        ON CREATE SET n.identifier = $identifier, n.name = $name
        ON MATCH  SET n.identifier = $identifier, n.name = $name
        RETURN n
        """)
    Generation insertGeneration(@Param("id") Long id, @Param("identifier") String identifier, @Param("name") String name);


    @Query("""
        MATCH (s:Generation {id: $generationId})
        MATCH (t:Region {id: $regionId})
        MERGE (s)-[:MAIN_REGION]->(t)
        """)
    void linkGenerationToRegion(@Param("generationId") Long generationId,
                        @Param("regionId") Long regionId);

    @Query("""
        MATCH (g:Generation {id: $generationId})
        MATCH (v:VersionGroup {id: $versionGroupId})
        MERGE (v)-[:INTRODUCED_IN]->(g)
        """)
    void linkGenerationToVersionGroup(@Param("generationId") Long generationId,
                        @Param("versionGroupId") Long versionGroupId);
}
