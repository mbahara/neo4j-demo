package at.jku.faw.neo4jdemo.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.neo4j.configuration.connectors.BoltConnector;
import org.neo4j.configuration.helpers.SocketAddress;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Neo4jEmbeddedServer {

	private static final Logger logger = LoggerFactory.getLogger(Neo4jEmbeddedServer.class);

	@Value("${neo4j-demo.db.path:db-data}")
	private String dbPath;
	@Value("${server.port}")
	private String serverPort;
	@Value("${spring.neo4j.uri}")
	private String neo4jUrl;
	private DatabaseManagementService managementService;
	public static final String SESSION_KEY = java.util.UUID.randomUUID().toString().substring(0, 10);

	@PostConstruct
	public void startNeo4j() {
		Path databaseDirectory = Paths.get(dbPath).toAbsolutePath();
		File dbDirFile = databaseDirectory.toFile();

		if (dbDirFile.exists()) {
			logger.info("Existing database files found at {}. Loading pre-populated db...", databaseDirectory);
		} else {
			logger.warn("No database found at {}. A new one will be created.", databaseDirectory);
		}

		String hostname = "localhost";
		int neo4jPort = 7687;

		try {
			managementService = new DatabaseManagementServiceBuilder(databaseDirectory)
					.setConfig(BoltConnector.enabled, true)
					.setConfig(BoltConnector.listen_address, new SocketAddress(hostname, neo4jPort))
					.build();

			GraphDatabaseService graphDatabase = managementService.database("neo4j");

			if(graphDatabase.isAvailable()) {
				logger.info("Neo4j is now available on {}.", neo4jUrl);
				printHeader(hostname, neo4jPort, databaseDirectory);
				logger.warn("Press [CTRL]+[C] to shutdown.");
			} else {
				logger.error("Neo4j failed to start.");
			}
		} catch (Exception e) {
			logger.error("FATAL: Neo4j failed to start: {}", e.getMessage());
			throw new RuntimeException(e);
		}
	}

	private void printHeader(String hostname, int neo4jPort, Path path){
		System.out.println("\n" + "=".repeat(40));
		System.out.println("   NEO4J DEMO - LOCAL SERVER IS ACTIVE");
		System.out.println("=".repeat(40));
		System.out.printf("Database Path: %s\n", path);
		System.out.printf("Endpoint: http://%s:%s/api/load/pokemon\n", hostname, serverPort);
		System.out.printf("Bolt UI:  bolt://%s:%s\n", hostname, neo4jPort);
		System.out.println("SESSION KEY: " + SESSION_KEY);
		System.out.println("=".repeat(40));
		System.out.println("Paste the key above into your UI or body of the request calls.\n");

	}

	@PreDestroy
	public void stopNeo4j() {
		if (managementService != null) {
			logger.warn("Shutting DB down...");
			managementService.shutdown();
		}
	}
}
