package com.dipvision.lora.common.exception

class CustomException(val detail: ExceptionDetail, vararg val formats: Any?) : RuntimeException()
