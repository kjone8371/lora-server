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
import org.springframework.web.bind.annotation.*

@RestController
//@CrossOrigin(origins = ["https://4b78-218-233-244-111.ngrok-free.app", "https://the-one-led.vercel.app", "https://front-end-git-main-kyumin1219s-projects.vercel.app"])
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {
//    @CrossOrigin(origins = ["https://4b78-218-233-244-111.ngrok-free.app/auth/login", "https://the-one-led.vercel.app/auth/login", "https://front-end-git-main-kyumin1219s-projects.vercel.app/auth/login"])
    @PostMapping("/login")
    fun authenticate(@RequestBody @Valid request: AuthenticateRequest): ResponseEntity<ResponseData<TokenResponse>> {
        val dto = authService.authenticate(request.credential, request.password)
        return ResponseData.ok(data = TokenResponse(dto.accessToken, dto.refreshToken, dto.passwordChangeAlert))
    }

//    @CrossOrigin(origins = ["https://4b78-218-233-244-111.ngrok-free.app/auth/register", "https://the-one-led.vercel.app/auth/register", "https://front-end-git-main-kyumin1219s-projects.vercel.app/auth/register"])
    @PostMapping("/register")
    fun createMember(@RequestBody @Valid request: CreateMemberRequest): ResponseEntity<ResponseData<Long>> {
        val userId = authService.createNewMember(
            request.name,
            request.credential,
            request.password,
            request.phone,
        )

        return ResponseData.ok(data = userId)
    }

//    @CrossOrigin(origins = ["https://4b78-218-233-244-111.ngrok-free.app/auth/password", "https://the-one-led.vercel.app/auth/password", "https://front-end-git-main-kyumin1219s-projects.vercel.app/auth/password"])
    @PatchMapping("/password")
    @SecurityRequirement(name = "Authorization")
    fun changePassword(@RequestBody @Valid request: ChangePasswordRequest): ResponseEntity<ResponseData<TokenResponse>> {
        val dto = authService.editPassword(request.oldPassword, request.newPassword)
        return ResponseData.ok(data = TokenResponse(dto.accessToken, dto.refreshToken, dto.passwordChangeAlert))
    }

//    @CrossOrigin(origins = ["https://4b78-218-233-244-111.ngrok-free.app/auth/refresh", "https://the-one-led.vercel.app/auth/refresh", "https://front-end-git-main-kyumin1219s-projects.vercel.app/auth/refresh"])
    @PostMapping("/refresh")
    @SecurityRequirement(name = "Authorization")
    fun refreshAccessToken(@RequestBody @Valid request: NewTokenRequest): ResponseEntity<ResponseData<TokenResponse>> {
        val dto = authService.getNewToken(request.refreshToken)
        return ResponseData.ok(data = TokenResponse(dto.accessToken, dto.refreshToken, dto.passwordChangeAlert))
    }
}
