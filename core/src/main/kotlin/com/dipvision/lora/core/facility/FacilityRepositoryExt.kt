package com.dipvision.lora.core.facility

import com.dipvision.lora.common.exception.CustomException
import com.dipvision.lora.core.common.id.WrappedLong
import com.dipvision.lora.core.facility.exception.FacilityExceptionDetails
import com.dipvision.lora.core.facility.repository.FacilityJpaRepository

fun FacilityJpaRepository.findSafe(id: Long) = WrappedLong(id).fetch(this)
    ?: throw CustomException(FacilityExceptionDetails.FACILITY_NOT_FOUND)