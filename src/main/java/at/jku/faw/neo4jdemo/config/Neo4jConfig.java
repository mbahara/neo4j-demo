package at.jku.faw.neo4jdemo.config;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.core.DatabaseSelectionProvider;
import org.springframework.data.neo4j.core.transaction.Neo4jTransactionManager;

@Configuration
public class Neo4jConfig {
	@Value("${spring.neo4j.uri}")
	private String neo4jUrl;

	@Value("${spring.neo4j.uri}")
	private String username;

	@Value("${spring.neo4j.uri}")
	private String password;

	@Bean(destroyMethod = "close")
	public Driver getDriver() {
		return GraphDatabase.driver(neo4jUrl, AuthTokens.basic(username, password));
	}

	@Bean
	public Neo4jTransactionManager transactionManager(Driver driver, DatabaseSelectionProvider databaseSelectionProvider) {
		return new Neo4jTransactionManager(driver, databaseSelectionProvider);
	}
}
