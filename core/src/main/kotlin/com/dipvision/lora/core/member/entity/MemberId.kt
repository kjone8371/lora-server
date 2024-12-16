package com.dipvision.lora.core.member.entity

import com.dipvision.lora.core.member.repository.MemberJpaRepository
import kotlin.jvm.optionals.getOrNull

@JvmInline
value class MemberId(val id: Long) {
    fun fetch(repository: MemberJpaRepository) = repository.findById(id).getOrNull()
}
