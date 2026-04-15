package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.MoveFlag;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MoveFlagRepository extends Neo4jRepository<MoveFlag, Long> {
    Optional<MoveFlag> findByIdentifier(String identifier);

    
    @Query("""
    UNWIND $rows AS row
    MERGE (n:MoveFlag {id: row.id})
    SET n.identifier = row.identifier
    RETURN count(n)
    """)
    Integer batchInsertMoveFlags(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:MoveFlag {id: $id})
        ON CREATE SET n.identifier = $identifier
        ON MATCH  SET n.identifier = $identifier
        RETURN n
        """)
    MoveFlag insertMoveFlag(@Param("id") Long id, @Param("identifier") String identifier);
}
