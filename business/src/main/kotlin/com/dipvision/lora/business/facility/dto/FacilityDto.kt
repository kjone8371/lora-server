package com.dipvision.lora.business.facility.dto

import com.dipvision.lora.business.facility.consts.FacilityStatus
import com.dipvision.lora.business.facility.consts.FacilityType
import org.springframework.web.multipart.MultipartFile

data class FacilityDto(
    val id: Long,
    
    val name: String,
    val type: FacilityType,
    val status: FacilityStatus,
    
    val address: String,
    val latitude: Double,
    val longitude: Double,
    
//    val filterOne: String?,
//    val filterTwo: String?,
//    val qr: String?,

    val meterNumber: String?,
    val department: String?,
    val fixture: String?,
    val poleFormat: String?,
    val dimmer: String?,

    val imageFilename: String,   // 이미지 파일명
    val memo: String?,

)
