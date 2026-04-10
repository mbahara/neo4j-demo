package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.PokemonShape;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PokemonShapeRepository extends Neo4jRepository<PokemonShape, Long> {
    Optional<PokemonShape> findByName(String name);

    @Query("""
        MERGE (n:PokemonShape {id: $id})
        ON CREATE SET n.name = $name, n.awesomeName = $awesomeName, n.description = $description
        ON MATCH  SET n.name = $name, n.awesomeName = $awesomeName, n.description = $description
        RETURN n
        """)
    PokemonShape insertPokemonShape(@Param("id") Long id, @Param("name") String name, @Param("awesomeName") int awesomeName, @Param("description") String description);
}
