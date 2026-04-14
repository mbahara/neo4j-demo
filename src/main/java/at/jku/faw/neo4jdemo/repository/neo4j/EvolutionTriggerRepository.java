package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.EvolutionTrigger;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EvolutionTriggerRepository extends Neo4jRepository<EvolutionTrigger, Long> {
    Optional<EvolutionTrigger> findByIdentifier(String identifier);

    
    @Query("""
    UNWIND $rows AS row
    MERGE (n:EvolutionTrigger {id: row.id})
    SET n.identifier = row.identifier
    """)
    void batchInsertEvolutionTriggers(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:EvolutionTrigger {id: $id})
        ON CREATE SET n.identifier = $identifier
        ON MATCH  SET n.identifier = $identifier
        RETURN n
        """)
    EvolutionTrigger insertEvolutionTrigger(@Param("id") Long id, @Param("identifier") String identifier);


}
