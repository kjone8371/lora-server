package com.dipvision.lora.infra.swagger

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI() = OpenAPI().apply {
        components(
            Components()
                .addSecuritySchemes(
                    "Authorization",
                    SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                )
        )
        info(
            Info()
                .title("Lora Server API")
                .version("v1.0.0")
        )
    }
}