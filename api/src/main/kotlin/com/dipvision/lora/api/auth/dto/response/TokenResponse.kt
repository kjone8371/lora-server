package com.dipvision.lora.api.auth.dto.response

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    
    val passwordChangeAlert: Boolean
)
