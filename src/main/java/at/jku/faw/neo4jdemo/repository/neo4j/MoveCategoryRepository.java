package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.MoveCategory;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MoveCategoryRepository extends Neo4jRepository<MoveCategory, Long> {
    Optional<MoveCategory> findByIdentifier(String identifier);

    @Query("""
        MERGE (n:MoveCategory {id: $id})
        ON CREATE SET n.identifier = $identifier, n.description = $description
        ON MATCH  SET n.identifier = $identifier, n.description = $description
        RETURN n
        """)
    MoveCategory insertMoveCategory(@Param("id") Long id, @Param("identifier") String identifier, @Param("description") String description);
}
