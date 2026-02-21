package com.nsteuerberg.gametracker.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DocumentationConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Game Tracker API Documentation")
                        .version("0.1.0")
                        .description(getDescription())
                        .contact(new Contact()
                                .name("Nicolás Rodríguez Steuerberg")
                                .email("nicolassteuerberg@gmail.com")
                                .url("https://github.com/NicolasRodriguezSteuerberg/GameTracker")
                        )
                        // Licencia mas famosa de código abierto
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")
                        )
                )
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Servidor Local (DEV)")
                ))
                // para poder probar con autenticación
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createSecurityScheme())
                        .addSchemas("ProblemDetail", new Schema()
                                .type("object")
                                .addProperty("title", new StringSchema().example("Resource Not Found"))
                                .addProperty("detail", new StringSchema().example("Game with ID 123 not found"))
                                .addProperty("instance", new StringSchema().example("/api/games/123"))
                                .addProperty("status", new IntegerSchema().example(404))
                        )
                );
    }

    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    // Descripción estilo en Markdown
    private String getDescription() {
        return """
        Welcome to the official GameTracker API documentation. This API allows users to manage their personal video games library, track progress, score games...
        
        > **Note** This API uses **IGDB** to sync data and provide rich metadata.
        
        ## Legal & Attribution
        This application uses data from **IGDB** but is not endorsed or certified by IGDB.
        * **Data Source:** [IGDB.com](https://www.igdb.com)
        * **Terms:** Use of this data is subject to IGDB's API Terms of Service.
        
        ## Authentication
        This API uses **JWT** for security.
        1. Authenticate via `/auth/login` (using your Google token id) to receive your **Access Token**.
        2. Click the **Authorize** button at the top right.
        3. Enter your token to unlock protected endpoints.
        
        ## Usage Guidelines
        To ensure best performance and UX, this api uses hybrid approach for identifaction
        * **Reading (GET):** Use `slugs` (e.g.: `/games/elden-ring`) for human-readable URLs.
        * **Writing  (POST/PATCH/DELETE):** Use numeric `IDs` (e.g: `/users/me/library/152`) to ensure data integrity and performance.
        
        ## Features
        * **Library Management:** Track statuses (Playing, Completed, etc.), manage play time and rate games.
        * **IGDB Integration:** Rich metadata including covers, screenshots, videos...
        """;
    }
}
