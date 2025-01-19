package com.dipvision.lora.api.remote.controller.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class RemoteRequest(
    @field:NotBlank
    val credential: String,

    @field:NotBlank
    val password: String,

    @field:NotBlank
    @field:Pattern(regexp = "^01[016789]-?[0-9]{3,4}-?[0-9]{4}$")
    val phone: String,

)