package com.dipvision.lora.api.facility.dto

import com.dipvision.lora.business.facility.consts.FacilityStatus
import com.dipvision.lora.business.facility.consts.FacilityType
import jakarta.validation.constraints.NotBlank

data class FacilityEditRequest(
    @field:NotBlank
    val name: String?,
    val type: FacilityType?,
    val status: FacilityStatus?,

    @field:NotBlank
    val address: String?,
    val latitude: Double?,
    val longitude: Double?,

    val meterNumber: String?,
    val department: String?,
    val fixture: String?,
    val poleFormat: String?,
    val dimmer: String?,

    val imageFilename: String?,  // 수정된 이미지 파일명
    val memo: String?,

)
