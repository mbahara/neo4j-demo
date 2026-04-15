package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.MoveTarget;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MoveTargetRepository extends Neo4jRepository<MoveTarget, Long> {
    Optional<MoveTarget> findByIdentifier(String identifier);
    Optional<MoveTarget> findByName(String name);

    
    @Query("""
    UNWIND $rows AS row
    MERGE (n:MoveTarget {id: row.id})
    SET n.identifier = row.identifier,
        n.name = row.name,
        n.description = row.description
    RETURN count(n)
    """)
    Integer batchInsertMoveTargets(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:MoveTarget {id: $id})
        ON CREATE SET n.identifier = $identifier, n.name = $name, n.description = $description
        ON MATCH  SET n.identifier = $identifier, n.name = $name, n.description = $description
        RETURN n
        """)
    MoveTarget insertMoveTarget(@Param("id") Long id, @Param("identifier") String identifier, @Param("name") String name, @Param("description") String description);
}
