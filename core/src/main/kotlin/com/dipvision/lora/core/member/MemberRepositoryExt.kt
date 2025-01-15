package com.dipvision.lora.core.member

import com.dipvision.lora.common.exception.CustomException
import com.dipvision.lora.core.common.id.WrappedLong
import com.dipvision.lora.core.member.entity.Member
import com.dipvision.lora.core.member.exception.MemberExceptionDetails
import com.dipvision.lora.core.member.repository.MemberJpaRepository

fun MemberJpaRepository.findSafe(id: Long): Member {
    return WrappedLong(id).fetch(this)
        ?: throw CustomException(MemberExceptionDetails.USER_NOT_FOUND)
}