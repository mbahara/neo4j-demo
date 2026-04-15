package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.ItemCategory;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemCategoryRepository extends Neo4jRepository<ItemCategory, Long> {
    Optional<ItemCategory> findByIdentifier(String identifier);
    Optional<ItemCategory> findByName(String name);

    
    @Query("""
    UNWIND $rows AS row
    MERGE (n:ItemCategory {id: row.id})
    SET n.identifier = row.identifier,
        n.name = row.name
    RETURN count(n)
    """)
    Integer batchInsertItemCategories(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:ItemCategory {id: $id})
        ON CREATE SET n.identifier = $identifier, n.name = $name
        ON MATCH  SET n.identifier = $identifier, n.name = $name
        RETURN n
        """)
    ItemCategory insertItemCategory(@Param("id") Long id, @Param("identifier") String identifier, @Param("name") String name);


    @Query("""
        MATCH (s:ItemCategory {id: $itemCategoryId})
        MATCH (t:ItemPocket {id: $itemPocketId})
        MERGE (s)-[:IN_POCKET]->(t)
        """)
    void linkItemCategoryToItemPocket(@Param("itemCategoryId") Long itemCategoryId,
                        @Param("itemPocketId") Long itemPocketId);

    @Query("CREATE INDEX itemcategory_id_idx IF NOT EXISTS FOR (n:ItemCategory) ON (n.id)")
    void createIdIndex();
}
