package com.dipvision.lora.common.response

sealed interface BaseResponse {
    val code: String
    val status: Int
}
