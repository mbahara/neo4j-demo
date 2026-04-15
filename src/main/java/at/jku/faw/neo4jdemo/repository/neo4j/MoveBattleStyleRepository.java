package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.MoveBattleStyle;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MoveBattleStyleRepository extends Neo4jRepository<MoveBattleStyle, Long> {
    Optional<MoveBattleStyle> findByIdentifier(String identifier);
    Optional<MoveBattleStyle> findByName(String name);

    
    @Query("""
    UNWIND $rows AS row
    MERGE (n:MoveBattleStyle {id: row.id})
    SET n.identifier = row.identifier,
        n.name = row.name
    RETURN count(n)
    """)
    Integer batchInsertMoveBattleStyles(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:MoveBattleStyle {id: $id})
        ON CREATE SET n.identifier = $identifier, n.name = $name
        ON MATCH  SET n.identifier = $identifier, n.name = $name
        RETURN n
        """)
    MoveBattleStyle insertMoveBattleStyle(@Param("id") Long id, @Param("identifier") String identifier, @Param("name") String name);

    @Query("CREATE INDEX movebattlestyle_id_idx IF NOT EXISTS FOR (n:MoveBattleStyle) ON (n.id)")
    void createIdIndex();
}
