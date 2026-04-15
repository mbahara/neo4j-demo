package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.Characteristic;
import java.util.List;
import java.util.Map;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacteristicRepository extends Neo4jRepository<Characteristic, Long> {

    
    @Query("""
    UNWIND $rows AS row
    MERGE (n:Characteristic {id: row.id})
    SET n.description = row.description
    RETURN count(n)
    """)
    Integer batchInsertCharacteristics(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:Characteristic {id: $id})
        ON CREATE SET n.description = $description
        ON MATCH  SET n.description = $description
        RETURN n
        """)
    Characteristic insertCharacteristic(@Param("id") Long id, @Param("description") String description);
}
