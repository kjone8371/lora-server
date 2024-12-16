package com.dipvision.lora.core.common.id

@JvmInline
value class WrappedLong(private val _id: Long?) : IdWrapper<Long> {
    override val id: Long get() = _id!!

    companion object {
        val NULL = WrappedLong(null)
    }
}
