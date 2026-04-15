package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.MoveMethod;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MoveMethodRepository extends Neo4jRepository<MoveMethod, Long> {
    Optional<MoveMethod> findByIdentifier(String identifier);
    Optional<MoveMethod> findByName(String name);

    
    @Query("""
    UNWIND $rows AS row
    MERGE (n:MoveMethod {id: row.id})
    SET n.identifier = row.identifier,
        n.name = row.name,
        n.description = row.description
    RETURN count(n)
    """)
    Integer batchInsertMoveMethods(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:MoveMethod {id: $id})
        ON CREATE SET n.identifier = $identifier, n.name = $name, n.description = $description
        ON MATCH  SET n.identifier = $identifier, n.name = $name, n.description = $description
        RETURN n
        """)
    MoveMethod insertMoveMethod(@Param("id") Long id, @Param("identifier") String identifier, @Param("name") String name, @Param("description") String description);


}
