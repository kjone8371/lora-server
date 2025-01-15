package com.dipvision.lora.core.member.service

import com.dipvision.lora.core.member.MemberHolder
import com.dipvision.lora.business.member.dto.MemberDto
import com.dipvision.lora.business.member.service.MemberService
import com.dipvision.lora.core.member.toDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true, rollbackFor = [Exception::class])
class MemberServiceImpl(
    private val memberHolder: MemberHolder
) : MemberService {
    override fun me(): MemberDto {
        return memberHolder.get().toDto()
    }
}
