package com.dipvision.lora.infra.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        // 특정 도메인만 허용하거나 모든 도메인을 허용할 수 있습니다.
        registry.addMapping("/**") // 모든 경로에 대해 CORS 허용
            .allowedOrigins("http://localhost:3000", "https://1e5a-218-233-244-111.ngrok-free.app", "https://the-one-led.vercel.app") // 허용할 도메인들
            .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메소드들
            .allowedHeaders("*") // 모든 헤더 허용
            .allowCredentials(true) // 인증 정보 허용
    }
}
