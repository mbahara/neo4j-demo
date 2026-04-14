package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.PokemonAbility;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface PokemonAbilityRepository extends Neo4jRepository<PokemonAbility, Long> {
	@Query("""
        MATCH (s:Pokemon {id: $pokemonId})
        MATCH (t:Ability {id: $abilityId})
        MERGE (s)-[:CAN_HAVE {isHidden: $isHidden, slot: $slot}]->(t)
        """)
	void linkPokemonToAbility(@Param("pokemonId") Long pokemonId, @Param("abilityId") Long abilityId, @Param("isHidden") boolean isHidden, @Param("slot") int slot);
}
