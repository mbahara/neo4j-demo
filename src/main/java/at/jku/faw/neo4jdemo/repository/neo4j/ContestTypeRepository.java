package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.ContestType;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestTypeRepository extends Neo4jRepository<ContestType, Long> {
    Optional<ContestType> findByIdentifier(String identifier);

    
    @Query("""
    UNWIND $rows AS row
    MERGE (n:ContestType {id: row.id})
    SET n.identifier = row.identifier
    RETURN count(n)
    """)
    Integer batchInsertContestTypes(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:ContestType {id: $id})
        ON CREATE SET n.identifier = $identifier
        ON MATCH  SET n.identifier = $identifier
        RETURN n
        """)
    ContestType insertContestType(@Param("id") Long id, @Param("identifier") String identifier);

    @Query("CREATE INDEX contesttype_id_idx IF NOT EXISTS FOR (n:ContestType) ON (n.id)")
    void createIdIndex();
}
