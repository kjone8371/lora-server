package com.dipvision.lora.core.group.exception

import com.dipvision.lora.common.exception.ExceptionDetail
import org.springframework.http.HttpStatus

enum class GroupExceptionDetails(override val status: HttpStatus, override val message: String) : ExceptionDetail {
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "그룹을 찾을 수 없습니다."),
    ;
    
    override val code = name
}