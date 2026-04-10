package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.EncounterMethod;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EncounterMethodRepository extends Neo4jRepository<EncounterMethod, Long> {
    Optional<EncounterMethod> findByIdentifier(String identifier);
    Optional<EncounterMethod> findByName(String name);

    @Query("""
        MERGE (n:EncounterMethod {id: $id})
        ON CREATE SET n.identifier = $identifier, n.name = $name, n.order = $order
        ON MATCH  SET n.identifier = $identifier, n.name = $name, n.order = $order
        RETURN n
        """)
    EncounterMethod insertEncounterMethod(@Param("id") Long id, @Param("identifier") String identifier, @Param("name") String name, @Param("order") int order);
}
