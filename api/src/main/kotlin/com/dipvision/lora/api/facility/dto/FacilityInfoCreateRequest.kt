package com.dipvision.lora.api.facility.dto

import com.dipvision.lora.business.facility.consts.FacilityStatus
import com.dipvision.lora.business.facility.consts.FacilityType
import jakarta.validation.constraints.NotBlank

data class FacilityInfoCreateRequest (
    @field:NotBlank
    val lightingType: String,
    val memo: String,
    val image: String,
    val fixture: String,
    val poleFormat: String,
    val poleNumber: String,
    val department: String,
    val streetAddress: String,
    val meterNumber: String,
    val dimmer: String,

    val facilityId: Long  // facilityId를 추가
)