package org.example.demo3.global.config;   // ← 실제 경로에 맞게 변경

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().openapi("3.0.0")
                .components(new Components()
                        .addSecuritySchemes("jwt-token",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Authorization")))
                .addSecurityItem(new SecurityRequirement().addList("jwt-token"))
                .info(apiInfo());
    }

    private Info apiInfo() {
        String projectName = "naver-food-wishlist";
        return new Info()
                .title(projectName + " Swagger")
                .description(projectName + " REST API Swagger Documentation")
                .version("1.0.0");
    }
}

