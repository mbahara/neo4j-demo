package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.PokemonType;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface PokemonTypeRepository extends Neo4jRepository<PokemonType, Long> {
	@Query("""
        MATCH (s:Pokemon {id: $pokemonId})
        MATCH (t:Type {id: $typeId})
        MERGE (s)-[:HAS_TYPE {slot: $slot}]->(t)
        """)
	void linkPokemonToType(@Param("pokemonId") Long pokemonId, @Param("typeId") Long typeId, @Param("slot") int slot);


    @Query("CREATE INDEX pokemontype_id_idx IF NOT EXISTS FOR (n:PokemonType) ON (n.id)")
    void createIdIndex();
}
