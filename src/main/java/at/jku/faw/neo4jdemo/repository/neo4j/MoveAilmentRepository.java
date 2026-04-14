package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.MoveAilment;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MoveAilmentRepository extends Neo4jRepository<MoveAilment, Long> {
    Optional<MoveAilment> findByIdentifier(String identifier);
    Optional<MoveAilment> findByName(String name);

    
    @Query("""
    UNWIND $rows AS row
    MERGE (n:MoveAilment {id: row.id})
    SET n.identifier = row.identifier,
        n.name = row.name
    """)
    void batchInsertMoveAilments(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:MoveAilment {id: $id})
        ON CREATE SET n.identifier = $identifier, n.name = $name
        ON MATCH  SET n.identifier = $identifier, n.name = $name
        RETURN n
        """)
    MoveAilment insertMoveAilment(@Param("id") Long id, @Param("identifier") String identifier, @Param("name") String name);
}
