package com.dipvision.lora.infra.config

import com.dipvision.lora.infra.musicismylife.MultipartJackson2HttpMessageConverter
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.http.converter.HttpMessageConverter


@Configuration
class WebConfig(
    private val multipartJackson2HttpMessageConverter: MultipartJackson2HttpMessageConverter

) : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        // 특정 도메인만 허용하거나 모든 도메인을 허용할 수 있습니다.
        registry.addMapping("/**") // 모든 경로에 대해 CORS 허용
            .allowedOrigins("http://localhost:3000", "https://693a-58-237-120-22.ngrok-free.app", "https://the-one-led.vercel.app", "http://192.168.45.23:3000/") // 허용할 도메인들
            .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메소드들
            .allowedHeaders("*") // 모든 헤더 허용
            .allowCredentials(true) // 인증 정보 허용
    }

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        // 기존의 Jackson2HttpMessageConverter을 포함한 다른 컨버터들에 multipart 변환기를 추가
        converters.add(multipartJackson2HttpMessageConverter)
    }
}
