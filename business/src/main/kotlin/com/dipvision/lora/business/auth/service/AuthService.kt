package com.dipvision.lora.business.auth.service

import com.dipvision.lora.business.auth.dto.TokenDto

interface AuthService {
    fun authenticate(credential: String, password: String): TokenDto
    fun createNewMember(name: String, credential: String, password: String, phone: String): Long
    fun editPassword(oldPassword: String, newPassword: String): TokenDto
    fun getNewToken(refreshToken: String): TokenDto
}
