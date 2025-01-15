package com.dipvision.lora.core.facility.service

import com.dipvision.lora.business.facility.dto.FacilityInfoDto
import com.dipvision.lora.core.facility.entity.FacilityInfo

fun FacilityInfo.toDto() = FacilityInfoDto(
    lightingType = this.lightingType ?: "",  // nullable 처리
    memo = this.memo ?: "",  // nullable 처리
    image = this.image ?: "",  // nullable 처리
    fixture = this.fixture ?: "",  // nullable 처리
    poleFormat = this.poleFormat ?: "",  // nullable 처리
    poleNumber = this.poleNumber ?: "",  // nullable 처리
    department = this.department ?: "",  // nullable 처리
    streetAddress = this.streetAddress ?: "",  // nullable 처리
    meterNumber = this.meterNumber ?: "",  // nullable 처리
    dimmer = this.dimmer ?: "",  // nullable 처리
    id = this.id ?: 0L  // null일 경우 0L을 기본값으로 사용
)