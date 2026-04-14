package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.PokemonGameIndex;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface PokemonGameIndexRepository extends Neo4jRepository<PokemonGameIndex, Long> {
	@Query("""
        MATCH (s:Pokemon {id: $pokemonId})
        MATCH (t:Version {id: $versionId})
        MERGE (s)-[:HAS_GAME_INDEX {index: $index}]->(t)
        """)
	void linkPokemonHasGameIndex(@Param("pokemonId") Long pokemonId, @Param("versionId") Long versionId, @Param("index") int index);

}
