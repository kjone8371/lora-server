package com.dipvision.lora.infra.security.jwt

import org.springframework.security.core.Authentication


//jwt를 검증하기 위한 인터페이스
interface TokenValidator {
    fun validate(jwt: String): Authentication
}
