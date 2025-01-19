package com.dipvision.lora.business.image.service

import com.dipvision.lora.business.image.dto.ImageDto

interface ImageService {
    fun readImage(id: String): ByteArray
    fun getImageInfo(id: String): ImageDto
}