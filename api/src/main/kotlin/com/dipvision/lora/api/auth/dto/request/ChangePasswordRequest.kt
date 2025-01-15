package com.dipvision.lora.api.auth.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class ChangePasswordRequest(
    @field:NotBlank
    @field:NotNull
    val oldPassword: String,
    @field:NotBlank
    @field:NotNull
    val newPassword: String
)