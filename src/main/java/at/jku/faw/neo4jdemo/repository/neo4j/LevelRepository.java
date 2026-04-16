package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.Level;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelRepository extends Neo4jRepository<Level, Long> {

    @Query("""
        MERGE (n:Level {value: $value})
        RETURN n
        """)
    Level insertLevel(@Param("value") int value);
}
