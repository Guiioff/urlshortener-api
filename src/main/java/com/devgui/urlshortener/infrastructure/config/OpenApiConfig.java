package com.devgui.urlshortener.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("URL Shortener API")
                        .description(
                                """
                                 URL Shortener API
                                """
                        )
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Guilherme")
                                .email("Guilherme@gmail.com"))
                        .license(new License()
                                .name("MIT")
                                .url(""))
                )
                .servers(List.of(new Server()
                        .url("http://localhost:8080")));
    }
}
