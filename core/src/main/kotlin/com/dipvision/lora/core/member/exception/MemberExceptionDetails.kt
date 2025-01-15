package com.dipvision.lora.core.member.exception

import com.dipvision.lora.common.exception.ExceptionDetail
import org.springframework.http.HttpStatus

enum class MemberExceptionDetails(override val status: HttpStatus, override val message: String) : ExceptionDetail {
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없음"),
    
    ;
    override val code = name
}