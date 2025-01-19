package com.dipvision.lora.infra.security.jwt

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class JwtAuthToken : AbstractAuthenticationToken {
    private val principal: Any
    
    constructor(jwt: String) : super(emptyList()) {
        principal = jwt
        super.setAuthenticated(false)
    }
    
    constructor(userDetails: UserDetails, authorities: Set<String>) : super(authorities.map { SimpleGrantedAuthority(it) }) {
        principal = userDetails
        super.setAuthenticated(true)
    }
    
    override fun getCredentials(): Any? = null // credential is not needed on jwt

    override fun getPrincipal(): Any = principal

    override fun setAuthenticated(authenticated: Boolean) {
        // no bypass
    }
}
