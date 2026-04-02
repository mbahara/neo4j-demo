package at.jku.faw.neo4jdemo.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Comparator;
import org.neo4j.configuration.helpers.SocketAddress;
import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.configuration.connectors.BoltConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class Neo4jEmbeddedServer {

	private static final Logger logger = LoggerFactory.getLogger(Neo4jEmbeddedServer.class);
	private final File dbDir = new File("db-data");
	private DatabaseManagementService managementService;
	public static final String SESSION_KEY = java.util.UUID.randomUUID().toString().substring(0, 10);

	@Value("${server.port}")
	private String serverPort;
	@Value("${spring.neo4j.uri}")
	private String neo4jUrl;

	@PostConstruct
	public void startNeo4j() throws Exception {
		if (dbDir.exists()) {
			logger.error("The directory 'db-data' already exists. Please delete the \"db-data\" folder and try again.");
			System.exit(1);
		}
		Files.createDirectory(dbDir.toPath());

		logger.warn("Database location is {}", dbDir.toPath().toAbsolutePath());

		String hostname = "localhost";
		int neo4jPort = 7687;

		managementService = new DatabaseManagementServiceBuilder(dbDir.toPath())
				.setConfig(BoltConnector.enabled, true)
				.setConfig(BoltConnector.listen_address, new SocketAddress(hostname, neo4jPort)).build();

		GraphDatabaseService graphDatabase = managementService.database("neo4j");
		if(graphDatabase.isAvailable()) {
			logger.info("Neo4j is now available on {}.", neo4jUrl);

			System.out.println("\n" + "=".repeat(40));
			System.out.println("   NEO4J DEMO - LOCAL SERVER IS ACTIVE");
			System.out.println("=".repeat(40));
			System.out.printf(" Endpoint: http://%s:%s", hostname, serverPort);
			System.out.printf("  Bolt UI:  bolt://%s:%s", hostname, neo4jPort);
			System.out.println(" SESSION KEY: " + SESSION_KEY);
			System.out.println("=".repeat(40));
			System.out.println("Paste the key above into your UI.\n");

			logger.warn("Press [CTRL]+[C] to shutdown.");
		} else {
			logger.error("Neo4j failed to start.");
		}
	}

	@PreDestroy
	public void stopNeo4j() {
		if (managementService != null) {
			logger.warn("Shutting DB down...");
			managementService.shutdown();
			logger.warn("Deleting database folder.");
			try (var stream = Files.walk(dbDir.toPath())) {
				stream.sorted(Comparator.reverseOrder())
						.map(java.nio.file.Path::toFile)
						.forEach(File::delete);
			} catch (IOException e) {
				logger.error("Error deleting database directory {}: {}", dbDir.getAbsolutePath(), e.getMessage());
			}
		}
	}
}
