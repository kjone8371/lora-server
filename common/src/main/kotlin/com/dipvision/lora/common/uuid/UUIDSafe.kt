package com.dipvision.lora.common.uuid

import com.dipvision.lora.common.exception.CustomException
import com.dipvision.lora.common.exception.GlobalExceptionDetail
import java.util.*

fun UUIDSafe(uuid: String): UUID = try {
    UUID.fromString(uuid)
} catch (e: IllegalArgumentException) {
    throw CustomException(GlobalExceptionDetail.INVALID_UUID)
}