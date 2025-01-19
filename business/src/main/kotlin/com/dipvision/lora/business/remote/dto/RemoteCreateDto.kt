package com.dipvision.lora.business.remote.dto

import com.dipvision.lora.business.facility.consts.RemoteProvider

data class RemoteCreateDto(
    val type: RemoteProvider,

    val address: String,
    val port: Int,
    val username: String?,
    val password: String?,
    val phone: String,
)