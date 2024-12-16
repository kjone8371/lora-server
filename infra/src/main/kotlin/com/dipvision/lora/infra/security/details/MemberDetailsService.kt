package com.dipvision.lora.infra.security.details

import com.dipvision.lora.core.member.details.MemberDetails
import com.dipvision.lora.core.member.repository.MemberJpaRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class MemberDetailsService(
    private val memberRepository: MemberJpaRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val member = memberRepository.findMemberByCredential(username)
            ?: throw UsernameNotFoundException(username)

        return MemberDetails(member)
    }
}
