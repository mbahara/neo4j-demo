package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.Characteristic;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacteristicRepository extends Neo4jRepository<Characteristic, Long> {

    @Query("""
        MERGE (n:Characteristic {id: $id})
        ON CREATE SET n.description = $description
        ON MATCH  SET n.description = $description
        RETURN n
        """)
    Characteristic insertCharacteristic(@Param("id") Long id, @Param("description") String description);


    @Query("""
        MATCH (s:Characteristic {id: $characteristicId})
        MATCH (t:Stat {id: $statId})
        MERGE (s)-[:HIGHLIGHTS {geneMod5: $geneMod5}]->(t)
        """)
    void linkCharacteristicToStat(@Param("characteristicId") Long characteristicId, @Param("statId") Long statId, @Param("geneMod5") int geneMod5);
}
