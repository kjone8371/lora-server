package com.dipvision.lora.common.uuid

import com.dipvision.lora.common.exception.CustomException
import com.dipvision.lora.common.exception.GlobalExceptionDetails
import java.util.*

fun UUIDSafe(uuid: String): UUID = try {
    UUID.fromString(uuid)
} catch (e: IllegalArgumentException) {
    throw CustomException(GlobalExceptionDetails.INVALID_UUID)
}