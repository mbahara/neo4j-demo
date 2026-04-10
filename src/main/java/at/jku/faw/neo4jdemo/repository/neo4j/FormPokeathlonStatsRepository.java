package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.FormPokeathlonStats;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FormPokeathlonStatsRepository extends Neo4jRepository<FormPokeathlonStats, Long> {

    @Query("""
        MERGE (n:FormPokeathlonStats {id: $id})
        ON CREATE SET n.minimumStat = $minimumStat, n.baseStat = $baseStat, n.maximumStat = $maximumStat
        ON MATCH  SET n.minimumStat = $minimumStat, n.baseStat = $baseStat, n.maximumStat = $maximumStat
        RETURN n
        """)
    FormPokeathlonStats insertFormPokeathlonStats(@Param("id") Long id, @Param("minimumStat") int minimumStat, @Param("baseStat") int baseStat, @Param("maximumStat") int maximumStat);
}
