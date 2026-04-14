package at.jku.faw.neo4jdemo.service.jkuMap;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

@Service
public class JkuMapServiceImpl implements JkuMapService {
	private static final Logger log = LoggerFactory.getLogger(JkuMapServiceImpl.class);
	private final Neo4jClient neo4jClient;
	private final ResourceLoader resourceLoader;

	public JkuMapServiceImpl(Neo4jClient neo4jClient, ResourceLoader resourceLoader) {
		this.neo4jClient = neo4jClient;
		this.resourceLoader = resourceLoader;
	}

	public void loadJkuMap() {
		Resource dumpResource = resourceLoader.getResource("classpath:data/jkuMap.dump");

		if (dumpResource.exists()) {
			log.info("--- Dump file detected. Loading JKU Map graph... ---");
			try (InputStream is = dumpResource.getInputStream()) {
				String cypherDump = new String(is.readAllBytes(), StandardCharsets.UTF_8);
				neo4jClient.query(cypherDump).run();
				log.info("JKU Data restored from dump successfully.");
			} catch (IOException e) {
				log.error("Error reading dump file: {}.", e.getMessage());
			}
		}
	}
}
