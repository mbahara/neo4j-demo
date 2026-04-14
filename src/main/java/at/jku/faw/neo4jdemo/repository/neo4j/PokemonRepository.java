package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.Pokemon;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PokemonRepository extends Neo4jRepository<Pokemon, Long> {
    Optional<Pokemon> findByIdentifier(String identifier);

    
    @Query("""
    UNWIND $rows AS row
    MERGE (n:Pokemon {id: row.id})
    SET n.identifier = row.identifier,
        n.height = row.height,
        n.weight = row.weight,
        n.baseExperience = row.baseExperience,
        n.order = row.order,
        n.isDefault = row.isDefault
    """)
    void batchInsertPokemons(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:Pokemon {id: $id})
        ON CREATE SET n.identifier = $identifier, n.height = $height, n.weight = $weight, n.baseExperience = $baseExperience, n.order = $order, n.isDefault = $isDefault
        ON MATCH  SET n.identifier = $identifier, n.height = $height, n.weight = $weight, n.baseExperience = $baseExperience, n.order = $order, n.isDefault = $isDefault
        RETURN n
        """)
    Pokemon insertPokemon(@Param("id") Long id, @Param("identifier") String identifier, @Param("height") int height, @Param("weight") int weight, @Param("baseExperience") int baseExperience, @Param("order") int order, @Param("isDefault") boolean isDefault);

    @Query("""
        MATCH (s:Pokemon {id: $pokemonId})
        MATCH (t:HeldItem {id: $heldItemId})
        MERGE (s)-[:HOLDS_ITEM]->(t)
        """)
    void linkPokemonToHeldItem(@Param("pokemonId") Long pokemonId,
                        @Param("heldItemId") Long heldItemId);

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
}
