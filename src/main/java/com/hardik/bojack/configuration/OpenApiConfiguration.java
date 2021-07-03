package com.hardik.bojack.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hardik.bojack.configuration.properties.OpenApiConfigurationProperties;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import lombok.AllArgsConstructor;

@Configuration
@EnableConfigurationProperties(OpenApiConfigurationProperties.class)
@AllArgsConstructor
public class OpenApiConfiguration {

	private final OpenApiConfigurationProperties openApiConfigurationProperties;

	@Bean
	public OpenAPI customOpenAPI() {
		final var properties = openApiConfigurationProperties.getSwagger();
		final var contact = properties.getContact();
		final var info = new Info().title(properties.getTitle()).version(properties.getApiVersion())
				.description(properties.getDescription())
				.contact(new Contact().email(contact.getEmail()).name(contact.getName()).url(contact.getUrl()));

		return new OpenAPI().info(info);
	}

}