package com.dipvision.lora.core.common.id

import java.util.UUID

@JvmInline
value class WrappedUUID(private val _id: UUID?) : IdWrapper<UUID> {
    override val get: UUID get() = _id!!

    companion object {
        val NULL = WrappedUUID(null)
    }
}
