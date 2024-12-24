package com.dipvision.lora.common.page

data class SlicedResponse<T>(
    val hasNext: Boolean,
    val size: Int,
    val data: List<T>
)