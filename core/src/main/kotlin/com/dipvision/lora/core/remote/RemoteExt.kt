package com.dipvision.lora.core.remote

import com.dipvision.lora.business.remote.dto.RemoteDto
import com.dipvision.lora.core.remote.entity.Remote

fun Remote.toDto() = RemoteDto(
    id.get,
    provider,
    address,
    phone
)