package com.dipvision.lora.core.group

import com.dipvision.lora.business.group.dto.GroupDto
import com.dipvision.lora.core.group.entity.Group
import com.dipvision.lora.core.member.toDto

fun Group.toDto() = GroupDto(
    id = id.get,
    name = name,
    availablePermissions = permission.toList(),
    members = members.map { it.toDto() }
)