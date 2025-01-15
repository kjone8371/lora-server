package com.dipvision.lora.api.group.dto.request

import com.dipvision.lora.common.permission.Permissions

data class GroupPermissionEditRequest(
    val permissions: List<Permissions>
)