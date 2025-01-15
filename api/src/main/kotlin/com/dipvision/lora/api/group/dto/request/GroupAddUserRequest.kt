package com.dipvision.lora.api.group.dto.request

import com.fasterxml.jackson.annotation.JsonCreator

data class GroupAddUserRequest @JsonCreator constructor(
    val userId: Long
)