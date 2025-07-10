package com.dietergandalf.store_manager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Store Manager API")
                        .version("1.0.0")
                        .description("API documentation for Store Manager Backend")
                        .contact(new Contact()
                                .name("Store Manager Team")
                                .email("contact@mbothe.de")
                                .url("https://mbothe.de"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:9080")
                                .description("Development server"),
                        new Server()
                                .url("http://localhost:8080")
                                .description("Container server")
                ));
    }
}
