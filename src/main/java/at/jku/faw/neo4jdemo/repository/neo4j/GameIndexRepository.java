package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.GameIndex;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface GameIndexRepository extends Neo4jRepository<GameIndex, Long> {
	@Query("""
        MATCH (s:Item {id: $itemId})
        MATCH (t:Generation {id: $generationId})
        MERGE (s)-[:HAS_GAME_INDEX {gameIndex: $gameIndex}]->(t)
        """)
	void linkItemHasGameIndex(@Param("itemId") Long itemId, @Param("generationId") Long generationId, @Param("gameIndex") int gameIndex);

	@Query("""
        MATCH (s:Location {id: $locationId})
        MATCH (t:Generation {id: $generationId})
        MERGE (s)-[:HAS_GAME_INDEX {gameIndex: $gameIndex}]->(t)
        """)
	void linkLocationHasGameIndex(@Param("locationId") Long locationId, @Param("generationId") Long generationId, @Param("gameIndex") int gameIndex);

	@Query("""
        MATCH (s:PokemonForm {id: $pokemonFormId})
        MATCH (t:Generation {id: $generationId})
        MERGE (s)-[:APPEARS_IN {gameIndex: $gameIndex}]->(t)
        """)
	void linkPokemonFormHasGameIndex(@Param("pokemonFormId") Long pokemonFormId, @Param("generationId") Long generationId, @Param("gameIndex") int gameIndex);

	@Query("""
        MATCH (s:Type {id: $typeId})
        MATCH (t:Generation {id: $generationId})
        MERGE (s)-[:HAS_INDEX {gameIndex: $gameIndex}]->(t)
        """)
	void linkTypeHasIndex(@Param("typeId") Long typeId, @Param("generationId") Long generationId, @Param("gameIndex") int gameIndex);
}
