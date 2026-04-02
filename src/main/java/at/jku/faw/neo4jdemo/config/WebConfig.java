package at.jku.faw.neo4jdemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${cors.allowed.origins}")
	private String corsAllowedOrigins;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		String[] origins = corsAllowedOrigins.split(",");

		registry.addMapping("/**")
				.allowedOriginPatterns(origins)
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
				.allowedHeaders("*")
				.allowCredentials(true);
	}
}
