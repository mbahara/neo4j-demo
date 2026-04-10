package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.EvolutionChain;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EvolutionChainRepository extends Neo4jRepository<EvolutionChain, Long> {

    @Query("""
        MERGE (n:EvolutionChain {id: $id})
        ON MATCH  SET n.id = $id
        RETURN n
        """)
    EvolutionChain insertEvolutionChain(@Param("id") Long id);


    @Query("""
        MATCH (s:EvolutionChain {id: $evolutionChainId})
        MATCH (t:Item {id: $itemId})
        MERGE (s)-[:BABY_TRIGGER_ITEM]->(t)
        """)
    void linkEvolutionChainToItem(@Param("evolutionChainId") Long evolutionChainId,
                        @Param("itemId") Long itemId);

    @Query("""
        MATCH (e:EvolutionChain {id: $evolutionChainId})
        MATCH (s:PokemonSpecies {id: $pokemonSpeciesId})
        MERGE (e)-[:HAS_MEMBER]->(s)
        """)
    void linkEvolutionChainToPokemonSpecies(@Param("evolutionChainId") Long evolutionChainId,
                        @Param("pokemonSpeciesId") Long pokemonSpeciesId);
}
