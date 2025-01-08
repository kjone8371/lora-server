package com.dipvision.lora.infra.security.jwt

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class JwtAuthToken : AbstractAuthenticationToken {
    private val credentials: Any
    
    constructor(jwt: String) : super(emptyList()) {
        credentials = jwt
        super.setAuthenticated(false)
    }
    
    constructor(userDetails: UserDetails, authorities: Set<String>) : super(authorities.map { SimpleGrantedAuthority(it) }) {
        credentials = userDetails
        super.setAuthenticated(true)
    }
    
    override fun getCredentials(): Any = credentials

    override fun getPrincipal(): Any? = null// principal is not needed on jwt

    override fun setAuthenticated(authenticated: Boolean) {
        // no bypass
    }
}
