package org.desafio.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Desafio Backend - API de Votação")
                        .description("API para gerenciamento de pautas, sessões de votação e votos.")
                        .version("1.0.0"));
    }
}
