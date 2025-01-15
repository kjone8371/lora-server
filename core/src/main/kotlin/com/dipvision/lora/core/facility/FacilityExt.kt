package com.dipvision.lora.core.facility

import com.dipvision.lora.business.facility.dto.FacilityDto
import com.dipvision.lora.business.facility.dto.FacilityInfoDto
import com.dipvision.lora.core.facility.entity.Facility
import com.dipvision.lora.core.facility.entity.FacilityInfo

fun Facility.toDto() = FacilityDto(

    id.get,

    name,
    type,
    status,

    address,
    latitude,
    longitude,

//    filter1,
//    filter2,
//    qr,

    meterNumber,
    department,
    fixture,
    poleFormat,
    dimmer,

    image.filename,  // 이미지를 파일명으로
    memo

)