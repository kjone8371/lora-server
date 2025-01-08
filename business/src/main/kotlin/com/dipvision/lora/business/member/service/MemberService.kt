package com.dipvision.lora.business.member.service

import com.dipvision.lora.business.member.dto.MemberDto

interface MemberService {
    fun me(): MemberDto
}
