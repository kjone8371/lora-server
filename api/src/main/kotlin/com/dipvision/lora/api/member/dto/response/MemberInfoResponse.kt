package com.dipvision.lora.api.member.dto.response

import com.dipvision.lora.common.permission.Permissions

data class MemberInfoResponse(
    val name: String,
    val phone: String,
    val groupName: String?,
    val permissions: List<Permissions>,
    val id: Long,
)
