package com.dipvision.lora.infra.security.config

import com.dipvision.lora.infra.exception.AuthExceptionHandleFilter
import com.dipvision.lora.infra.security.jwt.JwtAuthFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val authFilter: JwtAuthFilter,
    private val authExceptionFilter: AuthExceptionHandleFilter,
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .httpBasic { it.disable() }
            .csrf { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .sessionManagement { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/auth/password", "/auth/refresh").authenticated()
                    .requestMatchers("/auth/**").anonymous() // .permitAll()
                    .requestMatchers("/facilities/**").authenticated()
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterAt(authFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(authExceptionFilter, JwtAuthFilter::class.java)
            .build()
    }

    @Bean
    fun corsConfigurationSource() = UrlBasedCorsConfigurationSource()
        .apply {
            registerCorsConfiguration(
                "/**",
                CorsConfiguration()
                    .apply {
                        addAllowedOriginPattern("http://localhost:3000")
                        addAllowedOriginPattern("https://the-one-led.vercel.app")
                        addAllowedOriginPattern("https://front-end-git-main-kyumin1219s-projects.vercel.app")
                        addAllowedOriginPattern("http://localhost:8080")
                        addAllowedOriginPattern("https://693a-58-237-120-22.ngrok-free.app/") // ngrok 주소 허용
                        addAllowedOriginPattern("http://192.168.45.23:3000/")
                        addAllowedHeader("*")
                        addAllowedMethod("*")
                        allowCredentials = true
                    }
            )
        }
}
