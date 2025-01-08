package com.dipvision.lora.business.group.dto

import com.dipvision.lora.business.member.dto.MemberDto
import com.dipvision.lora.common.permission.Permissions
import java.util.UUID

data class GroupDto(
    val id: UUID,
    val name: String,
    val availablePermissions: List<Permissions>,
    val members: List<MemberDto>
)
