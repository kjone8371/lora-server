package com.dipvision.lora.business.member.dto

import com.dipvision.lora.common.permission.Permissions

data class MemberDto(
    val name: String,
    val phone: String,
    val groupName: String?,
    val permissions: List<Permissions>,
    val id: Long,
)
