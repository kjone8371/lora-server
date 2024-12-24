package com.dipvision.lora.api.auth.presentation

import com.dipvision.lora.api.auth.dto.request.AuthenticateRequest
import com.dipvision.lora.api.auth.dto.request.ChangePasswordRequest
import com.dipvision.lora.api.auth.dto.request.CreateMemberRequest
import com.dipvision.lora.api.auth.dto.request.NewTokenRequest
import com.dipvision.lora.api.auth.dto.response.TokenResponse
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
    fun authenticate(@RequestBody @Valid request: AuthenticateRequest): ResponseEntity<ResponseData<TokenResponse>> {
        val dto = authService.authenticate(request.credential, request.password)
        return ResponseData.ok(data = TokenResponse(dto.accessToken, dto.refreshToken, dto.passwordChangeAlert))
    }

    @PostMapping("/register")
    fun createMember(@RequestBody @Valid request: CreateMemberRequest): ResponseEntity<ResponseData<Long>> {
        val userId = authService.createNewMember(request.name, request.credential, request.password)
        return ResponseData.ok(data = userId)
    }

    @PostMapping("/refresh")
    @SecurityRequirement(name = "Authorization")
    fun refreshAccessToken(@RequestBody @Valid request: NewTokenRequest): ResponseEntity<ResponseData<TokenResponse>> {
        val dto = authService.getNewToken(request.refreshToken)
        return ResponseData.ok(data = TokenResponse(dto.accessToken, dto.refreshToken, dto.passwordChangeAlert))
    }
}
