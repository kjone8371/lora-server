package com.dipvision.lora.api.auth.dto.request

import jakarta.validation.constraints.NotBlank



data class AuthenticateRequest(
    @field:NotBlank
    val credential: String,
    @field:NotBlank
    val password: String
)
