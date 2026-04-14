package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.EggGroup;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EggGroupRepository extends Neo4jRepository<EggGroup, Long> {
    Optional<EggGroup> findByIdentifier(String identifier);
    Optional<EggGroup> findByName(String name);

    
    @Query("""
    UNWIND $rows AS row
    MERGE (n:EggGroup {id: row.id})
    SET n.identifier = row.identifier,
        n.name = row.name
    """)
    void batchInsertEggGroups(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:EggGroup {id: $id})
        ON CREATE SET n.identifier = $identifier, n.name = $name
        ON MATCH  SET n.identifier = $identifier, n.name = $name
        RETURN n
        """)
    EggGroup insertEggGroup(@Param("id") Long id, @Param("identifier") String identifier, @Param("name") String name);
}
