package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.PokemonHabitat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PokemonHabitatRepository extends Neo4jRepository<PokemonHabitat, Long> {
    Optional<PokemonHabitat> findByIdentifier(String identifier);
    
    @Query("""
    UNWIND $rows AS row
    MERGE (n:PokemonHabitat {id: row.id})
    SET n.identifier = row.identifier
    RETURN count(n)
    """)
    Integer batchInsertPokemonHabitats(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:PokemonHabitat {id: $id})
        ON CREATE SET n.identifier = $identifier
        ON MATCH  SET n.identifier = $identifier
        RETURN n
        """)
    PokemonHabitat insertPokemonHabitat(@Param("id") Long id, @Param("identifier") String identifier);


    @Query("""
        MATCH (s:PokemonHabitat {id: $pokemonHabitatId})
        MATCH (t:Region {id: $regionId})
        MERGE (s)-[:LOCATED_IN]->(t)
        """)
    void linkPokemonHabitatToRegion(@Param("pokemonHabitatId") Long pokemonHabitatId,
                        @Param("regionId") Long regionId);
}
