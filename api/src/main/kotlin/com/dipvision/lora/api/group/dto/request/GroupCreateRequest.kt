package com.dipvision.lora.api.group.dto.request

import com.dipvision.lora.common.permission.Permissions
import jakarta.validation.constraints.NotBlank

data class GroupCreateRequest(
    @field:NotBlank
    val name: String,
    val permissions: List<Permissions>
)