package com.dipvision.lora.core.facility

import com.dipvision.lora.business.facility.dto.FacilityDto
import com.dipvision.lora.core.facility.entity.Facility

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

    department,
    fixture,
    poleFormat,
    dimmer,

    image?.id?.get?.toString(),
    memo,
    phoneNumber,
    escoStatus,
    powerConsumption,
    billingType

)