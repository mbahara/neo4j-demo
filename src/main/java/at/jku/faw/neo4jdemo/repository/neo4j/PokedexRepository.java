package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.Pokedex;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PokedexRepository extends Neo4jRepository<Pokedex, Long> {
    Optional<Pokedex> findByIdentifier(String identifier);

    
    @Query("""
    UNWIND $rows AS row
    MERGE (n:Pokedex {id: row.id})
    SET n.identifier = row.identifier,
        n.isMainSeries = row.isMainSeries
    RETURN count(n)
    """)
    Integer batchInsertPokedexs(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:Pokedex {id: $id})
        ON CREATE SET n.identifier = $identifier, n.isMainSeries = $isMainSeries
        ON MATCH  SET n.identifier = $identifier, n.isMainSeries = $isMainSeries
        RETURN n
        """)
    Pokedex insertPokedex(@Param("id") Long id, @Param("identifier") String identifier, @Param("isMainSeries") boolean isMainSeries);


    @Query("""
        MATCH (s:Pokedex {id: $pokedexId})
        MATCH (t:Region {id: $regionId})
        MERGE (s)-[:REGION_SCOPE]->(t)
        """)
    void linkPokedexToRegion(@Param("pokedexId") Long pokedexId, @Param("regionId") Long regionId);

    @Query("""
        MATCH (s:Pokedex {id: $pokedexId})
        MATCH (t:VersionGroup {id: $versionGroupId})
        MERGE (s)-[:USED_IN]->(t)
        """)
    void linkPokedexToVersionGroup(@Param("pokedexId") Long pokedexId,
                        @Param("versionGroupId") Long versionGroupId);

    @Query("CREATE INDEX pokedex_id_idx IF NOT EXISTS FOR (n:Pokedex) ON (n.id)")
    void createIdIndex();
}
