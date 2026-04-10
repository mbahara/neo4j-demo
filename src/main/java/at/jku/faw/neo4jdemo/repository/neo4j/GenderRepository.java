package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.Gender;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GenderRepository extends Neo4jRepository<Gender, Long> {
    Optional<Gender> findByIdentifier(String identifier);

    @Query("""
        MERGE (n:Gender {id: $id})
        ON CREATE SET n.identifier = $identifier
        ON MATCH  SET n.identifier = $identifier
        RETURN n
        """)
    Gender insertGender(@Param("id") Long id, @Param("identifier") String identifier);


}
