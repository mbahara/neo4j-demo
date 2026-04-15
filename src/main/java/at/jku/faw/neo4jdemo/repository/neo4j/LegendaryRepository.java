package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.Legendary;
import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LegendaryRepository extends Neo4jRepository<Legendary, Long> {

    @Query("""
        MERGE (n:Legendary {id: $id})
        ON MATCH SET n.id = $id
        RETURN n
        """)
    Legendary insertLegendary(@Param("id") Long id);

    @Query("""
        UNWIND $ids AS id
        MERGE (l:Legendary {id: id})
    """)
    Integer batchInsertLegendary(List<Long> ids);
}
