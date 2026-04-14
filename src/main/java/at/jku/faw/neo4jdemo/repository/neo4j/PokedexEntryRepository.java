package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.PokedexEntry;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface PokedexEntryRepository extends Neo4jRepository<PokedexEntry, Long> {
	@Query("""
        MATCH (s:PokemonSpecies {id: $pokemonSpeciesId})
        MATCH (t:Pokedex {id: $pokedexId})
        MERGE (s)-[:HAS_POKEDEX {pokedexNr: $pokedexNr}]->(t)
        """)
	void linkPokemonSpeciesToPokedex(@Param("pokemonSpeciesId") Long pokemonSpeciesId,
									 @Param("pokedexId") Long pokedexId,
									 @Param("pokedexNr") int pokedexNr);
}
