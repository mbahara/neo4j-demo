package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.ChangeEvent;
import java.util.List;
import java.util.Map;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeEventRepository extends Neo4jRepository<ChangeEvent, Long> {

    
    @Query("""
    UNWIND $rows AS row
    MERGE (n:ChangeEvent {id: row.id})
    SET n.effect = row.effect
    """)
    void batchInsertChangeEvents(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:ChangeEvent {id: $id})
        ON CREATE SET n.effect = $effect
        ON MATCH  SET n.effect = $effect
        RETURN n
        """)
    ChangeEvent insertChangeEvent(@Param("id") Long id, @Param("effect") String effect);


    @Query("""
        MATCH (s:ChangeEvent {id: $changeEventId})
        MATCH (t:VersionGroup {id: $versionGroupId})
        MERGE (s)-[:OCCURRED_IN]->(t)
        """)
    void linkChangeEventToVersionGroup(@Param("changeEventId") Long changeEventId,
                        @Param("versionGroupId") Long versionGroupId);
}
