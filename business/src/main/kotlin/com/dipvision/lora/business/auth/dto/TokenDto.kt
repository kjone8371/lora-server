package com.dipvision.lora.business.auth.dto

data class TokenDto(
    val accessToken: String,
    val refreshToken: String,
    
    val passwordChangeAlert: Boolean
)
