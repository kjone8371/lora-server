package com.dipvision.lora.infra.image.impl

import com.dipvision.lora.core.image.delegate.ImageManager
import com.dipvision.lora.infra.image.ImageManagerProperties
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.UUID
import kotlin.io.path.*


//원래 있던 거(테스트중 (로컬로 저장))
//class LocalImageManager(
//    private val imageManagerProperties: ImageManagerProperties
//) : ImageManager {
//    private val savePath = Path("idiot").apply {
//        createDirectories()
//    }
//
//    override fun uploadImage(id: UUID, bytes: ByteArray): Boolean {
//        return try {
//            val path = Path(savePath.absolutePathString(), imageManagerProperties.bucketName, "$id")
//            path.writeBytes(bytes, StandardOpenOption.CREATE)
//
//            true
//        } catch (e: IOException) {
//            false
//        }
//    }
//
//    override fun readImage(id: UUID): ByteArray? {
//        val fileOrNull = Path(savePath.toString(), imageManagerProperties.bucketName, "$id")
//        if (!fileOrNull.exists() || !fileOrNull.isReadable()) return null
//        return fileOrNull.readBytes()
//    }
//}



// 새로 생긴거 테스트
class LocalImageManager(private val localDirectory: String) : ImageManager {

    override fun uploadImage(id: UUID, bytes: ByteArray): Boolean {
        return try {
            // 이미지 저장할 경로 생성
            val imageFile = File(localDirectory, "$id.jpg")  // UUID로 파일명을 설정하고 확장자 추가

            // 로컬 디렉토리가 없으면 생성
            val path = imageFile.parentFile
            if (!path.exists()) {
                path.mkdirs()
            }

            // 이미지 파일에 바이트 배열로 데이터를 저장
            Files.write(Paths.get(imageFile.absolutePath), bytes)

            println("Image saved to: ${imageFile.absolutePath}")
            true
        } catch (e: Exception) {
            println("Failed to save image: ${e.message}")
            false
        }
    }

    override fun readImage(id: UUID): ByteArray? {
        return try {
            // 파일 경로
            val imageFile = File(localDirectory, "$id.jpg")

            // 파일이 존재하면 바이트 배열로 읽기
            if (imageFile.exists()) {
                Files.readAllBytes(imageFile.toPath())
            } else {
                null
            }
        } catch (e: Exception) {
            println("Failed to read image: ${e.message}")
            null
        }
    }
}