package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.HeldItem;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HeldItemRepository extends Neo4jRepository<HeldItem, Long> {

    @Query("""
        MERGE (n:HeldItem)
        ON CREATE SET n.rarity = $rarity
        ON MATCH  SET n.rarity = $rarity
        RETURN n
        """)
    HeldItem insertHeldItem(@Param("rarity") int rarity);


    @Query("""
        MATCH (s:HeldItem {id: $heldItemId})
        MATCH (t:Item {id: $itemId})
        MERGE (s)-[:ITEM_HELD]->(t)
        """)
    void linkHeldItemToItem(@Param("heldItemId") Long heldItemId,
                        @Param("itemId") Long itemId);

    @Query("""
        MATCH (s:HeldItem {id: $heldItemId})
        MATCH (t:Version {id: $versionId})
        MERGE (s)-[:IN_VERSION]->(t)
        """)
    void linkHeldItemToVersion(@Param("heldItemId") Long heldItemId,
                        @Param("versionId") Long versionId);
}
