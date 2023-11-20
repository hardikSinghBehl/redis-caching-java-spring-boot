package com.behl.cachetropolis.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(OpenApiConfigurationProperties.class)
public class OpenApiConfiguration {

	private final OpenApiConfigurationProperties openApiConfigurationProperties;

	@Bean
	public OpenAPI openApi() {
		final var properties = openApiConfigurationProperties.getOpenApi();
		final var info = new Info()
				.title(properties.getTitle())
				.version(properties.getVersion())
				.description(properties.getDescription());

		return new OpenAPI().info(info);
	}
	
}