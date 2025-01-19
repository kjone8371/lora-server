package com.dipvision.lora.core.image

import com.dipvision.lora.business.image.dto.ImageDto
import com.dipvision.lora.core.image.entity.Image

fun Image.toDto() = ImageDto(
    fileName = fileName,
    id = id.get
)