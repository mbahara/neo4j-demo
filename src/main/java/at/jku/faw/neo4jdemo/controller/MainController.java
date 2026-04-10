package at.jku.faw.neo4jdemo.controller;

import at.jku.faw.neo4jdemo.service.Neo4jEmbeddedServer;
import at.jku.faw.neo4jdemo.service.jkuMap.JkuMapService;
import at.jku.faw.neo4jdemo.service.movie.MovieService;
import at.jku.faw.neo4jdemo.service.pokemon.PokemonLoaderService;
import java.util.Map;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MainController {

	private final SessionFactory sessionFactory;
	private final PokemonLoaderService pokemonLoaderService;
	private final JkuMapService jkuMapService;
	private final MovieService movieService;

	public MainController(SessionFactory sessionFactory, PokemonLoaderService pokemonLoaderService, JkuMapService jkuMapService,
						  MovieService movieService) {
		this.sessionFactory = sessionFactory;
		this.pokemonLoaderService = pokemonLoaderService;
		this.jkuMapService = jkuMapService;
		this.movieService = movieService;
	}

	@GetMapping("/health")
	public ResponseEntity<Void> healthCheck() {
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/clearDB")
	public ResponseEntity<String> clearDatabase(@RequestBody Map<String, String> body) {
		if (Neo4jEmbeddedServer.SESSION_KEY.equals(body.get("key"))) {
			sessionFactory.openSession().purgeDatabase();
			return ResponseEntity.ok("Database cleared!");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Key");
	}

	@PostMapping("/load/{dataset}")
	public ResponseEntity<String> loadPokemon(@PathVariable String dataset, @RequestBody Map<String, String> body) {
		if (Neo4jEmbeddedServer.SESSION_KEY.equals(body.get("key"))) {
			if ("pokemon".equalsIgnoreCase(dataset)) {
				pokemonLoaderService.loadPokemonData();
				return ResponseEntity.ok("Pokèmon Graph Generated Successfully");
			}

			if ("map".equalsIgnoreCase(dataset)) {
				jkuMapService.loadJkuMap();
				return ResponseEntity.ok("JKU Map Graph Generated Successfully");
			}

			if ("movie".equalsIgnoreCase(dataset)) {
				movieService.loadMovieDump();
				return ResponseEntity.ok("Movie Graph Generated Successfully");
			}

			return ResponseEntity.badRequest().body("Unknown dataset");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Key");
	}
}
