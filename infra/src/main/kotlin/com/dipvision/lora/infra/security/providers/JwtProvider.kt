package com.dipvision.lora.infra.security.providers

import com.dipvision.lora.common.exception.CustomException
import com.dipvision.lora.common.exception.GlobalExceptionDetails
import com.dipvision.lora.infra.security.jwt.JwtAuthToken
import com.dipvision.lora.infra.security.jwt.TokenValidator
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class JwtProvider(
    private val validator: TokenValidator
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication?): Authentication {
        val jwt = authentication?.credentials?.toString()
            ?: throw CustomException(GlobalExceptionDetails.INTERNAL_SERVER_ERROR)

        return validator.validate(jwt)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return JwtAuthToken::class.java == authentication
    }
}
