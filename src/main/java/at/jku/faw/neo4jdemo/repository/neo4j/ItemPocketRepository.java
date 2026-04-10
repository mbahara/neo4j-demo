package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.ItemPocket;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemPocketRepository extends Neo4jRepository<ItemPocket, Long> {
    Optional<ItemPocket> findByIdentifier(String identifier);
    Optional<ItemPocket> findByName(String name);

    @Query("""
        MERGE (n:ItemPocket {id: $id})
        ON CREATE SET n.identifier = $identifier, n.name = $name
        ON MATCH  SET n.identifier = $identifier, n.name = $name
        RETURN n
        """)
    ItemPocket insertItemPocket(@Param("id") Long id, @Param("identifier") String identifier, @Param("name") String name);
}
