package com.dipvision.lora.business.facility.dto

import com.dipvision.lora.business.facility.consts.RemoteProvider

data class FacilityRemoteInfoDto(
    val provider: RemoteProvider,
    val phone: String,
    val number: Int,
)