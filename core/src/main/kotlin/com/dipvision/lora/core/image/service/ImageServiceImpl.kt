package com.dipvision.lora.core.image.service

import com.dipvision.lora.business.image.dto.ImageDto
import com.dipvision.lora.business.image.service.ImageService
import com.dipvision.lora.core.image.toDto
import org.springframework.stereotype.Service

@Service
class ImageServiceImpl(
    private val imageInternalService: ImageInternalService,
) : ImageService {
    override fun readImage(id: String): ByteArray {
        return imageInternalService.readImage(id)
    }

    override fun getImageInfo(id: String): ImageDto {
        val image = imageInternalService.getImage(id)
        return image.toDto()
    }
}