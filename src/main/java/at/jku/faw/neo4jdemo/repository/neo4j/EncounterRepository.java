package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.Encounter;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EncounterRepository extends Neo4jRepository<Encounter, Long> {

    @Query("""
        MERGE (n:Encounter {id: $id})
        ON CREATE SET n.minLevel = $minLevel, n.maxLevel = $maxLevel
        ON MATCH  SET n.minLevel = $minLevel, n.maxLevel = $maxLevel
        RETURN n
        """)
    Encounter insertEncounter(@Param("id") Long id, @Param("minLevel") int minLevel, @Param("maxLevel") int maxLevel);


    @Query("""
        MATCH (s:Encounter {id: $encounterId})
        MATCH (t:Version {id: $versionId})
        MERGE (s)-[:IN_VERSION]->(t)
        """)
    void linkEncounterToVersion(@Param("encounterId") Long encounterId,
                        @Param("versionId") Long versionId);

    @Query("""
        MATCH (s:Encounter {id: $encounterId})
        MATCH (t:EncounterMethod {id: $encounterMethodId})
        MERGE (s)-[:VIA_METHOD]->(t)
        """)
    void linkEncounterToEncounterMethod(@Param("encounterId") Long encounterId,
                        @Param("encounterMethodId") Long encounterMethodId);

    @Query("""
        MATCH (s:Encounter {id: $encounterId})
        MATCH (t:Location {id: $locationAreaId})
        MERGE (s)-[:AT_LOCATION_AREA]->(t)
        """)
    void linkEncounterToLocationArea(@Param("encounterId") Long encounterId,
                                     @Param("locationAreaId") Long locationAreaId);
}
