package com.dipvision.lora.core.facility.exception

import com.dipvision.lora.common.exception.ExceptionDetail
import org.springframework.http.HttpStatus

enum class FacilityExceptionDetails(override val status: HttpStatus, override val message: String) : ExceptionDetail {
    FACILITY_NOT_FOUND(HttpStatus.NOT_FOUND, "시설물을 찾을 수 없음."),
    NO_REMOTE_INFO(HttpStatus.BAD_REQUEST, "시설물에 원격정보가 존재하지 않음."),
    TOGGLE_TIME_NOT_MATCH(HttpStatus.BAD_REQUEST, "시설물 점/소등 시간이 너무 긺. (0~180분이 요구되나, %s분을 받음)"),
    ;
    
    override val code = name
}