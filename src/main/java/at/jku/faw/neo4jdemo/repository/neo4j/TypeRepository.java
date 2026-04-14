package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends Neo4jRepository<Type, Long> {
    Optional<Type> findByIdentifier(String identifier);

    
    @Query("""
    UNWIND $rows AS row
    MERGE (n:Type {id: row.id})
    SET n.identifier = row.identifier
    """)
    void batchInsertTypes(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:Type {id: $id})
        ON CREATE SET n.identifier = $identifier
        ON MATCH  SET n.identifier = $identifier
        RETURN n
        """)
    Type insertType(@Param("id") Long id, @Param("identifier") String identifier);


    @Query("""
        MATCH (s:Type {id: $typeId})
        MATCH (t:Generation {id: $generationId})
        MERGE (s)-[:INTRODUCED_IN]->(t)
        """)
    void linkTypeToGeneration(@Param("typeId") Long typeId,
                        @Param("generationId") Long generationId);

    @Query("""
        MATCH (s:Type {id: $typeId})
        MATCH (t:DamageClass {id: $damageClassId})
        MERGE (s)-[:DEFAULT_DAMAGE_CLASS]->(t)
        """)
    void linkTypeToDamageClass(@Param("typeId") Long typeId,
                        @Param("damageClassId") Long damageClassId);
}
