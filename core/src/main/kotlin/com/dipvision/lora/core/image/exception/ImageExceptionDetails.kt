package com.dipvision.lora.core.image.exception

import com.dipvision.lora.common.exception.ExceptionDetail
import org.springframework.http.HttpStatus

enum class ImageExceptionDetails(override val message: String, override val status: HttpStatus) : ExceptionDetail {
    IMAGE_NOT_FOUND("그런 이미지는 없다.", HttpStatus.NOT_FOUND),
    IMAGE_UPLOAD_FAILED("이미지 업로드 중 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR)
    ;

    override val code: String = name
}