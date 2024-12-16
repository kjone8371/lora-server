package com.dipvision.lora.core.member.service

import com.dipvision.lora.core.member.MemberHolder
import com.dipvision.lora.business.member.dto.MemberDto
import com.dipvision.lora.business.member.service.MemberService
import org.springframework.stereotype.Service

@Service
class MemberServiceImpl(
    private val memberHolder: MemberHolder
) : MemberService {
    override fun me(): MemberDto {
        val member = memberHolder.get()
        return MemberDto(member.name)
    }
}
