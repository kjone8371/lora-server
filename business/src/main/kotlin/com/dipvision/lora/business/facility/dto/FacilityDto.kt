package com.dipvision.lora.business.facility.dto

import com.dipvision.lora.business.facility.consts.FacilityStatus
import com.dipvision.lora.business.facility.consts.FacilityType

data class FacilityDto(
    val id: Long,

    val name: String,
    val type: FacilityType,
    val status: FacilityStatus,

    val address: String,
    val latitude: Double,
    val longitude: Double,

    val filterOne: String?,
    val filterTwo: String?,
    val qr: String?,
)
