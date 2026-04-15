package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.PokemonMove;
import java.util.List;
import java.util.Map;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PokemonMoveRepository extends Neo4jRepository<PokemonMove, Long> {

    @Query("""
        UNWIND $rows AS row
        MERGE (n:PokemonMove)
        SET n.level = row.level, n.order = row.order
        """)
    void batchInsertPokemonMoves(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:PokemonMove)
        ON CREATE SET n.level = $level, n.order = $order
        ON MATCH  SET n.level = $level, n.order = $order
        RETURN n
        """)
    PokemonMove insertPokemonMove(@Param("level") int level, @Param("order") int order);


    @Query("""
        MATCH (s:PokemonMove {id: $pokemonMoveId})
        MATCH (t:Move {id: $moveId})
        MERGE (s)-[:MOVE_LEARNED]->(t)
        """)
    void linkPokemonMoveToMove(@Param("pokemonMoveId") Long pokemonMoveId,
                        @Param("moveId") Long moveId);

    @Query("""
        MATCH (s:PokemonMove {id: $pokemonMoveId})
        MATCH (t:MoveMethod {id: $moveMethodId})
        MERGE (s)-[:LEARNED_VIA]->(t)
        """)
    void linkPokemonMoveToMoveMethod(@Param("pokemonMoveId") Long pokemonMoveId,
                        @Param("moveMethodId") Long moveMethodId);

    @Query("""
        MATCH (s:PokemonMove {id: $pokemonMoveId})
        MATCH (t:VersionGroup {id: $versionGroupId})
        MERGE (s)-[:IN_VERSION_GROUP]->(t)
        """)
    void linkPokemonMoveToVersionGroup(@Param("pokemonMoveId") Long pokemonMoveId,
                        @Param("versionGroupId") Long versionGroupId);
}
