package com.dipvision.lora.api.auth.presentation

import com.dipvision.lora.api.auth.dto.request.AuthenticateRequestDto
import com.dipvision.lora.api.auth.dto.request.CreateMemberRequestDto
import com.dipvision.lora.api.auth.dto.request.NewTokenRequestDto
import com.dipvision.lora.api.auth.dto.response.TokenResponseDto
import com.dipvision.lora.business.auth.service.AuthService
import com.dipvision.lora.common.response.ResponseData
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/login")
    fun authenticate(@RequestBody @Valid request: AuthenticateRequestDto): ResponseEntity<ResponseData<TokenResponseDto>> {
        val dto = authService.authenticate(request.credential, request.password)
        return ResponseData.ok(data = TokenResponseDto(dto.accessToken, dto.refreshToken))
    }

    @PostMapping("/register")
    fun createMember(@RequestBody @Valid request: CreateMemberRequestDto): ResponseEntity<ResponseData<Long>> {
        val userId = authService.createNewMember(request.name, request.credential, request.password)
        return ResponseData.ok(data = userId)
    }

    @PostMapping("/refresh")
    @SecurityRequirement(name = "Authorization")
    fun refreshAccessToken(@RequestBody @Valid request: NewTokenRequestDto): ResponseEntity<ResponseData<TokenResponseDto>> {
        val dto = authService.getNewToken(request.refreshToken)
        return ResponseData.ok(data = TokenResponseDto(dto.accessToken, dto.refreshToken))
    }
}
