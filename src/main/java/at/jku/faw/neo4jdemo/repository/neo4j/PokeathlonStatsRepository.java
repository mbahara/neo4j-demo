package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.PokeathlonStats;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PokeathlonStatsRepository extends Neo4jRepository<PokeathlonStats, Long> {
    Optional<PokeathlonStats> findByIdentifier(String identifier);

    
    @Query("""
    UNWIND $rows AS row
    MERGE (n:PokeathlonStats {id: row.id})
    SET n.identifier = row.identifier
    RETURN count(n)
    """)
    Integer batchInsertPokeathlonStats(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:PokeathlonStats {id: $id})
        ON CREATE SET n.identifier = $identifier
        ON MATCH  SET n.identifier = $identifier
        RETURN n
        """)
    PokeathlonStats insertPokeathlonStats(@Param("id") Long id, @Param("identifier") String identifier);

    @Query("CREATE INDEX pokeathlonstats_id_idx IF NOT EXISTS FOR (n:PokeathlonStats) ON (n.id)")
    void createIdIndex();
}
