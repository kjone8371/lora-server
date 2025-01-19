package com.dipvision.lora.core.remote.exception

import com.dipvision.lora.common.exception.ExceptionDetail
import org.springframework.http.HttpStatus

enum class RemoteExceptionDetails(override val message: String, override val status: HttpStatus) : ExceptionDetail {
    REMOTE_NOT_FOUND("그런 원격 브로커는 없다.", HttpStatus.NOT_FOUND),
    ;

    override val code: String = name
}