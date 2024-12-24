package com.dipvision.lora.core.facility.exception

import com.dipvision.lora.common.exception.ExceptionDetail
import org.springframework.http.HttpStatus

enum class FacilityExceptionDetails(override val status: HttpStatus, override val message: String) : ExceptionDetail {
    FACILITY_NOT_FOUND(HttpStatus.NOT_FOUND, "시설물을 찾을 수 없음."),
    ;
    
    override val code = name
}