package com.dipvision.lora.core.image.service

import com.dipvision.lora.common.exception.CustomException
import com.dipvision.lora.common.multipart.guessExtension
import com.dipvision.lora.common.uuid.UUIDSafe
import com.dipvision.lora.core.common.id.WrappedUUID
import com.dipvision.lora.core.image.delegate.ImageManager
import com.dipvision.lora.core.image.entity.Image
import com.dipvision.lora.core.image.exception.ImageExceptionDetails
import com.dipvision.lora.core.image.repository.ImageJpaRepository
import org.springframework.stereotype.Service

@Service
class ImageInternalService(
    private val imageJpaRepository: ImageJpaRepository,
    private val imageManager: ImageManager,
) {
    fun saveImage(fileName: String, data: ByteArray): Image {
        val name = "$fileName.${guessExtension(data)}"
        val image = imageJpaRepository.save(Image(fileName = name))

        val isUploaded = imageManager.uploadImage(image.id.get, data)
        if (!isUploaded) {
            imageJpaRepository.delete(image)
            throw CustomException(ImageExceptionDetails.IMAGE_UPLOAD_FAILED)
        }

        return image
    }

    fun deleteImage(id: String) = imageJpaRepository.deleteById(UUIDSafe(id))

    fun readImage(id: String): ByteArray =
        imageManager.readImage(UUIDSafe(id)) ?: throw CustomException(ImageExceptionDetails.IMAGE_NOT_FOUND)

    fun getImage(id: String): Image {
        val image = WrappedUUID(UUIDSafe(id)).fetch(imageJpaRepository)
            ?: throw CustomException(ImageExceptionDetails.IMAGE_NOT_FOUND)

        return image
    }
}
