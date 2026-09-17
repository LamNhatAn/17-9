package com.example.categoryapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI categoryAppOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Category App API")
                .version("1.0")
                .description("REST and GraphQL APIs for Product and Category CRUD"));
    }
}