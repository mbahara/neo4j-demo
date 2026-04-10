package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.PokemonForm;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PokemonFormRepository extends Neo4jRepository<PokemonForm, Long> {
    Optional<PokemonForm> findByIdentifier(String identifier);

    @Query("""
        MERGE (n:PokemonForm {id: $id})
        ON CREATE SET n.identifier = $identifier, n.isDefault = $isDefault, n.isMega = $isMega, n.isBattleOnly = $isBattleOnly
        ON MATCH  SET n.identifier = $identifier, n.isDefault = $isDefault, n.isMega = $isMega, n.isBattleOnly = $isBattleOnly
        RETURN n
        """)
    PokemonForm insertPokemonForm(@Param("id") Long id, @Param("identifier") String identifier, @Param("isDefault") boolean isDefault, @Param("isMega") boolean isMega, @Param("isBattleOnly") boolean isBattleOnly);


    @Query("""
        MATCH (f:PokemonForm {id: $pokemonFormId})
        MATCH (p:Pokemon {id: $pokemonId})
        MERGE (p)-[:HAS_FORM]->(f)
        """)
    void linkPokemonFormToPokemon(@Param("pokemonFormId") Long pokemonFormId,
                        @Param("pokemonId") Long pokemonId);

    @Query("""
        MATCH (s:PokemonForm {id: $pokemonFormId})
        MATCH (t:PokeathlonStats {id: $pokeathlonStatsId})
        MERGE (s)-[:POKEATHLON_PERFORMANCE {minimumStat: $minimumStat, baseStat: $baseStat, maximumStat: $maximumStat}]->(t)
        """)
    void linkPokemonFormToPokeathlonStats(@Param("pokemonFormId") Long pokemonFormId, @Param("pokeathlonStatsId") Long pokeathlonStatsId, @Param("minimumStat") int minimumStat, @Param("baseStat") int baseStat, @Param("maximumStat") int maximumStat);

    @Query("""
        MATCH (s:PokemonForm {id: $pokemonFormId})
        MATCH (t:Generation {id: $generationId})
        MERGE (s)-[:APPEARS_IN {index: $index}]->(t)
        """)
    void linkPokemonFormHasGameIndex(@Param("pokemonFormId") Long pokemonFormId, @Param("generationId") Long generationId, @Param("index") int index);
}
