package com.dipvision.lora.api.facility.dto

import com.dipvision.lora.business.facility.consts.FacilityStatus
import com.dipvision.lora.business.facility.consts.FacilityType
import jakarta.validation.constraints.NotBlank
import org.springframework.web.multipart.MultipartFile

data class FacilityCreateRequest(
    @field:NotBlank
    val name: String,
    val type: FacilityType,
    val status: FacilityStatus,

    @field:NotBlank
    val address: String,
    val latitude: Double,
    val longitude: Double,

    val department: String,
    val fixture: String,
    val poleFormat: String,
    val dimmer: String,

    val memo: String,

    val phoneNumber: String,
    val escoStatus: String,
    val powerConsumption: String,
    val billingType: String,


)
