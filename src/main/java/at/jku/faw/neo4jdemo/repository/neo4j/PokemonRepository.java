package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.Pokemon;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PokemonRepository extends Neo4jRepository<Pokemon, Long> {
    Optional<Pokemon> findByIdentifier(String identifier);

    @Query("""
        MERGE (n:Pokemon {id: $id})
        ON CREATE SET n.identifier = $identifier, n.height = $height, n.weight = $weight, n.baseExperience = $baseExperience, n.order = $order, n.isDefault = $isDefault
        ON MATCH  SET n.identifier = $identifier, n.height = $height, n.weight = $weight, n.baseExperience = $baseExperience, n.order = $order, n.isDefault = $isDefault
        RETURN n
        """)
    Pokemon insertPokemon(@Param("id") Long id, @Param("identifier") String identifier, @Param("height") int height, @Param("weight") int weight, @Param("baseExperience") int baseExperience, @Param("order") int order, @Param("isDefault") boolean isDefault);


    @Query("""
        MATCH (s:Pokemon {id: $pokemonId})
        MATCH (t:Stats {id: $statsId})
        MERGE (s)-[:HAS_STAT {baseStat: $baseStat, effort: $effort}]->(t)
        """)
    void linkPokemonToStats(@Param("pokemonId") Long pokemonId, @Param("statsId") Long statsId, @Param("baseStat") int baseStat, @Param("effort") int effort);

    @Query("""
        MATCH (s:Pokemon {id: $pokemonId})
        MATCH (t:HeldItem {id: $heldItemId})
        MERGE (s)-[:HOLDS_ITEM]->(t)
        """)
    void linkPokemonToHeldItem(@Param("pokemonId") Long pokemonId,
                        @Param("heldItemId") Long heldItemId);

    @Query("""
        MATCH (s:Pokemon {id: $pokemonId})
        MATCH (t:Ability {id: $abilityId})
        MERGE (s)-[:CAN_HAVE {isHidden: $isHidden, slot: $slot}]->(t)
        """)
    void linkPokemonToAbility(@Param("pokemonId") Long pokemonId, @Param("abilityId") Long abilityId, @Param("isHidden") boolean isHidden, @Param("slot") int slot);


    @Query("""
        MATCH (s:Pokemon {id: $pokemonId})
        MATCH (t:PokemonSpecies {id: $pokemonSpeciesId})
        MERGE (s)-[:BELONGS_TO_SPECIES]->(t)
        """)
    void linkPokemonToPokemonSpecies(@Param("pokemonId") Long pokemonId,
                        @Param("pokemonSpeciesId") Long pokemonSpeciesId);

    @Query("""
        MATCH (s:Pokemon {id: $pokemonId})
        MATCH (t:PokemonMove {id: $pokemonMoveId})
        MERGE (s)-[:CAN_LEARN]->(t)
        """)
    void linkPokemonToPokemonMove(@Param("pokemonId") Long pokemonId,
                        @Param("pokemonMoveId") Long pokemonMoveId);

    @Query("""
        MATCH (s:Pokemon {id: $pokemonId})
        MATCH (t:Encounter {id: $encounterId})
        MERGE (s)-[:FOUND_IN]->(t)
        """)
    void linkPokemonToEncounter(@Param("pokemonId") Long pokemonId,
                        @Param("encounterId") Long encounterId);

    @Query("""
        MATCH (s:Pokemon {id: $pokemonId})
        MATCH (t:Version {id: $versionId})
        MERGE (s)-[:HAS_GAME_INDEX {index: $index}]->(t)
        """)
    void linkPokemonHasGameIndex(@Param("pokemonId") Long pokemonId, @Param("versionId") Long versionId, @Param("index") int index);

    @Query("""
        MATCH (s:Pokemon {id: $pokemonId})
        MATCH (t:Type {id: $typeId})
        MERGE (s)-[:HAS_TYPE {slot: $slot}]->(t)
        """)
    void linkPokemonToType(@Param("pokemonId") Long pokemonId, @Param("typeId") Long typeId, @Param("slot") int slot);
}
