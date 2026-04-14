package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.PalParkEncounter;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface PalParkEncounterRepository extends Neo4jRepository<PalParkEncounter, Long> {
	@Query("""
        MATCH (s:PokemonSpecies {id: $pokemonSpeciesId})
        MATCH (t:Area {id: $areaId})
        MERGE (s)-[r:FOUND_IN_PAL_PARK]->(t)
        SET r.baseScore = $baseScore, r.rate = $rate
        """)
	void linkPokemonSpeciesToPalParkArea(@Param("pokemonSpeciesId") Long pokemonSpeciesId,
										 @Param("areaId") Long areaId,
										 @Param("baseScore") int baseScore,
										 @Param("rate") int rate);
}
