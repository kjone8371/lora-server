package com.dipvision.lora.core.image.delegate

import java.util.*

interface ImageManager {
    fun uploadImage(id: UUID, bytes: ByteArray): Boolean // imageURL
    fun readImage(id: UUID): ByteArray?
}