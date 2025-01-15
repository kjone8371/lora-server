package com.dipvision.lora.core.group

import com.dipvision.lora.common.exception.CustomException
import com.dipvision.lora.core.group.exception.GroupExceptionDetails
import com.dipvision.lora.core.group.repository.GroupRepository
import org.springframework.data.repository.findByIdOrNull
import java.util.UUID

fun GroupRepository.findSafe(id: UUID) = findByIdOrNull(id)
    ?: throw CustomException(GroupExceptionDetails.GROUP_NOT_FOUND)