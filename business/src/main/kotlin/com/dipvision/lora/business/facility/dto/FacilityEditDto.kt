package com.dipvision.lora.business.facility.dto

import com.dipvision.lora.business.facility.consts.FacilityStatus
import com.dipvision.lora.business.facility.consts.FacilityType
import org.springframework.web.multipart.MultipartFile

data class FacilityEditDto(
    val name: String?,
    val type: FacilityType?,
    val status: FacilityStatus?,

    val address: String?,
    val latitude: Double?,
    val longitude: Double?,

    val department: String?,
    val fixture: String?,
    val poleFormat: String?,
    val dimmer: String?,

    val memo: String?,
    val phoneNumber: String?,
    val escoStatus: String?,
    val powerConsumption: String?,
    val billingType: String?,

)