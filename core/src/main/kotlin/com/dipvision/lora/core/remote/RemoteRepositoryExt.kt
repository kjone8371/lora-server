package com.dipvision.lora.core.remote

import com.dipvision.lora.common.exception.CustomException
import com.dipvision.lora.core.common.id.WrappedLong
import com.dipvision.lora.core.remote.exception.RemoteExceptionDetails
import com.dipvision.lora.core.remote.repository.RemoteJpaRepository

fun RemoteJpaRepository.findSafe(id: Long) = WrappedLong(id).fetch(this)
    ?: throw CustomException(RemoteExceptionDetails.REMOTE_NOT_FOUND)