package com.dipvision.lora.core.auth.service

import com.dipvision.lora.business.auth.dto.TokenDto
import com.dipvision.lora.core.auth.extern.TokenGenerator
import com.dipvision.lora.business.auth.service.AuthService
import com.dipvision.lora.common.exception.CustomException
import com.dipvision.lora.core.auth.exception.AuthExceptionDetails
import com.dipvision.lora.core.member.MemberHolder
import com.dipvision.lora.core.member.entity.Member
import com.dipvision.lora.core.member.repository.MemberJpaRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.Period

@Service
class AuthServiceImpl(
    private val memberRepository: MemberJpaRepository,
    private val authenticationManager: AuthenticationManager,
    private val passwordEncoder: PasswordEncoder,
    private val tokenGenerator: TokenGenerator,
    private val holder: MemberHolder,
) : AuthService {
    override fun authenticate(credential: String, password: String): TokenDto {
        val token = UsernamePasswordAuthenticationToken(credential, password)

        val auth = authenticationManager.authenticate(token)
        SecurityContextHolder.getContext().authentication = auth
        
        return generateToken()
    }

    override fun createNewMember(name: String, credential: String, password: String): Long {
        if (memberRepository.existsByCredential(credential))
            throw CustomException(AuthExceptionDetails.USER_ALREADY_EXISTS, credential)

        return memberRepository.save(Member(credential, passwordEncoder.encode(password), name))
            .id.id
    }

    override fun editPassword(oldPassword: String, newPassword: String): TokenDto {
        val me = holder.get()
        
        val oldRequest = UsernamePasswordAuthenticationToken(me.credential, oldPassword)
        authenticationManager.authenticate(oldRequest) // this will validate things
        
        memberRepository.save(
            me.copy(
                password = passwordEncoder.encode(newPassword)
            )
        )
        
        val newRequest = UsernamePasswordAuthenticationToken(me.credential, newPassword)
        val auth = authenticationManager.authenticate(newRequest)
        SecurityContextHolder.getContext().authentication = auth
        
        return generateToken(me)
    }

    override fun getNewToken(refreshToken: String): TokenDto {
        val newAccessToken = tokenGenerator.refreshToNewToken(refreshToken)
        
        return TokenDto(
            newAccessToken,
            refreshToken, // no changes ;)
            shouldChangePassword()
        )
    }
    
    private fun shouldChangePassword(me: Member = holder.get()): Boolean {
        val threeMonthsAgo = Instant.now().minus(Period.ofMonths(3))
        return threeMonthsAgo.isAfter(me.lastPasswordModified)
    }
    
    private fun generateToken(me: Member = holder.get()): TokenDto {
        val access = tokenGenerator.generateAccessToken()
        val refresh = tokenGenerator.generateRefreshToken()

        return TokenDto(access, refresh, shouldChangePassword(me))
    }
}
