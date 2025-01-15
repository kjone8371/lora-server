package com.dipvision.lora.business.group.dto

import com.dipvision.lora.common.permission.Permissions
import java.util.*

data class GroupPreviewDto(
    val id: UUID,
    val name: String,
    val availablePermissions: List<Permissions>,
)