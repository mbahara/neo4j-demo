package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.DamageClass;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DamageClassRepository extends Neo4jRepository<DamageClass, Long> {
    Optional<DamageClass> findByIdentifier(String identifier);
    Optional<DamageClass> findByName(String name);

    @Query("""
        MERGE (n:DamageClass {id: $id})
        ON CREATE SET n.identifier = $identifier, n.name = $name, n.description = $description
        ON MATCH  SET n.identifier = $identifier, n.name = $name, n.description = $description
        RETURN n
        """)
    DamageClass insertDamageClass(@Param("id") Long id, @Param("identifier") String identifier, @Param("name") String name, @Param("description") String description);
}
