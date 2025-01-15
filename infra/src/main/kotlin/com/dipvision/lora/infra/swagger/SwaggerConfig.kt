package com.dipvision.lora.infra.swagger

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

//@Configuration
//class SwaggerConfig {
//    @Bean
//    fun openAPI() = OpenAPI().apply {
//        components(
//            Components()
//                .addSecuritySchemes(
//                    "Authorization",
//                    SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
//                )
//        )
//        info(
//            Info()
//                .title("Lora Server API")
//                .version("v1.0.0")
//        )
//    }
//}

@Configuration
class SwaggerConfig {
    @Value("\${SPRING_SWAGGER_SERVER_URL:https://1e5a-218-233-244-111.ngrok-free.app}")
    private val serverUrl: String? = null

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            // 서버 URL 설정
            .servers(Collections.singletonList(Server().url(serverUrl).description("API Server")))
            // 보안 설정 (Authorization: Bearer JWT)
            .components(
                Components()
                    .addSecuritySchemes(
                        "Authorization",
                        SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                    )
            )
            // API 기본 정보 설정
            .info(
                Info()
                    .title("Lora Server API")
                    .version("v1.0.0")
                    .description("Lora Server API 명세서")
            )
    }
}
