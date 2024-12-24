package com.dipvision.lora.core.common.permission

import com.dipvision.lora.common.exception.CustomException
import com.dipvision.lora.common.exception.GlobalExceptionDetail
import com.dipvision.lora.common.permission.Permission
import com.dipvision.lora.common.permission.Permissions

fun Permission.validate(permission: Permissions) {
    if (permission !in this)
        throw CustomException(GlobalExceptionDetail.NO_ENOUGH_PERMISSION, permission)
}