package com.dipvision.lora.business.remote.dto

import com.dipvision.lora.business.facility.consts.RemoteProvider

data class RemoteDto(
    val id: Long,
    val provider: RemoteProvider,
    val address: String,
    val phone: String?,
)