package com.dipvision.lora.core.facility

import com.dipvision.lora.business.facility.dto.FacilityRemoteInfoDto
import com.dipvision.lora.core.facility.entity.FacilityRemoteInfo

fun FacilityRemoteInfo.toDto() = FacilityRemoteInfoDto(
    provider = remote.provider,
    phone = phone,
    number = number
)