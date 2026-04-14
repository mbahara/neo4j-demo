package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.ItemFlag;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemFlagRepository extends Neo4jRepository<ItemFlag, Long> {
    Optional<ItemFlag> findByIdentifier(String identifier);
    Optional<ItemFlag> findByName(String name);

    
    @Query("""
    UNWIND $rows AS row
    MERGE (n:ItemFlag {id: row.id})
    SET n.identifier = row.identifier,
        n.name = row.name,
        n.description = row.description
    """)
    void batchInsertItemFlags(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:ItemFlag {id: $id})
        ON CREATE SET n.identifier = $identifier, n.name = $name, n.description = $description
        ON MATCH  SET n.identifier = $identifier, n.name = $name, n.description = $description
        RETURN n
        """)
    ItemFlag insertItemFlag(@Param("id") Long id, @Param("identifier") String identifier, @Param("name") String name, @Param("description") String description);
}
