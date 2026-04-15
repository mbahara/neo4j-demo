package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.Nature;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NatureRepository extends Neo4jRepository<Nature, Long> {
    Optional<Nature> findByIdentifier(String identifier);

    
    @Query("""
    UNWIND $rows AS row
    MERGE (n:Nature {id: row.id})
    SET n.identifier = row.identifier
    RETURN count(n)
    """)
    Integer batchInsertNatures(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:Nature {id: $id})
        ON CREATE SET n.identifier = $identifier
        ON MATCH  SET n.identifier = $identifier
        RETURN n
        """)
    Nature insertNature(@Param("id") Long id, @Param("identifier") String identifier);


    @Query("""
        MATCH (s:Nature {id: $natureId})
        MATCH (t:Stat {id: $statId})
        MERGE (s)-[:INCREASES]->(t)
        """)
    void linkNatureIncreasesStat(@Param("natureId") Long natureId,
                        @Param("statId") Long statId);

    @Query("""
        MATCH (s:Nature {id: $natureId})
        MATCH (t:Stat {id: $statId})
        MERGE (s)-[:DECREASES]->(t)
        """)
    void linkNatureDecreasesStat(@Param("natureId") Long natureId,
                        @Param("statId") Long statId);

    @Query("CREATE INDEX nature_id_idx IF NOT EXISTS FOR (n:Nature) ON (n.id)")
    void createIdIndex();
}
