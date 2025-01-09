package com.dipvision.lora.core.auth.extern


// jwt를 생성하기 위한 인터페이스
interface TokenGenerator {
    fun generateAccessToken(): String
    fun generateRefreshToken(): String
    fun refreshToNewToken(refreshToken: String): String
}
