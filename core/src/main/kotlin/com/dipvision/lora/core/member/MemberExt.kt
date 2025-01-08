package com.dipvision.lora.core.member

import com.dipvision.lora.business.member.dto.MemberDto
import com.dipvision.lora.core.member.entity.Member

fun Member.toDto() = MemberDto(
    name = name,
    phone = phone,
    groupName = group?.name,
    permissions = group?.permission?.toList() ?: emptyList(),
    id = id.get,
)