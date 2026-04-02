package at.jku.faw.neo4jdemo.config;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.context.annotation.Configuration;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@Configuration
public class Neo4jConfig {
	@Value("${spring.neo4j.uri}")
	private String neo4jUrl;

	@Value("${spring.neo4j.uri}")
	private String username;

	@Value("${spring.neo4j.uri}")
	private String password;

	@Bean
	public org.neo4j.ogm.config.Configuration getConfiguration() {
		return new org.neo4j.ogm.config.Configuration.Builder()
				.uri(neo4jUrl)
				.credentials(username, password)
				.build();
	}

	@Bean(destroyMethod = "close")
	public Driver getDriver() {
		return GraphDatabase.driver(neo4jUrl, AuthTokens.basic(username, password));
	}

	@Bean
	public SessionFactory sessionFactory(org.neo4j.ogm.config.Configuration config) {
		return new SessionFactory(config, "at.jku.faw.neo4jdemo.model.neo4j");
	}
}
