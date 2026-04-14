package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.Item;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends Neo4jRepository<Item, Long> {
    Optional<Item> findByIdentifier(String identifier);
    Optional<Item> findByName(String name);

    @Query("""
        MERGE (n:Item {id: $id})
        ON CREATE SET n.identifier = $identifier, n.name = $name, n.cost = $cost, n.flingPower = $flingPower, n.shortEffect = $shortEffect, n.effect = $effect
        ON MATCH  SET n.identifier = $identifier, n.name = $name, n.cost = $cost, n.flingPower = $flingPower, n.shortEffect = $shortEffect, n.effect = $effect
        RETURN n
        """)
    Item insertItem(@Param("id") Long id, @Param("identifier") String identifier, @Param("name") String name, @Param("cost") int cost, @Param("flingPower") int flingPower, @Param("shortEffect") String shortEffect, @Param("effect") String effect);


    @Query("""
        MATCH (s:Item {id: $itemId})
        MATCH (t:ItemCategory {id: $itemCategoryId})
        MERGE (s)-[:BELONGS_TO]->(t)
        """)
    void linkItemToItemCategory(@Param("itemId") Long itemId,
                        @Param("itemCategoryId") Long itemCategoryId);

    @Query("""
        MATCH (s:Item {id: $itemId})
        MATCH (t:FlingEffect {id: $flingEffectId})
        MERGE (s)-[:HAS_FLING_EFFECT]->(t)
        """)
    void linkItemToFlingEffect(@Param("itemId") Long itemId,
                        @Param("flingEffectId") Long flingEffectId);

    @Query("""
        MATCH (s:Item {id: $itemId})
        MATCH (t:ItemFlag {id: $itemFlagId})
        MERGE (s)-[:HAS_FLAG]->(t)
        """)
    void linkItemToItemFlag(@Param("itemId") Long itemId,
                        @Param("itemFlagId") Long itemFlagId);

    @Query("""
        MATCH (s:Item {id: $itemId})
        MATCH (t:Generation {id: $generationId})
        MERGE (s)-[:HAS_GAME_INDEX {gameIndex: $gameIndex}]->(t)
        """)
    void linkItemHasGameIndex(@Param("itemId") Long itemId, @Param("generationId") Long generationId, @Param("gameIndex") int gameIndex);

    @Query("""
        MATCH (s:Item {id: $itemId})
        MATCH (t:Machine {id: $machineId})
        MERGE (s)-[:ACTS_AS_MACHINE]->(t)
        """)
    void linkItemToMachine(@Param("itemId") Long itemId,
                        @Param("machineId") Long machineId);
}
