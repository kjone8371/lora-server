package com.dipvision.lora.infra.image

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration


// 원래 꺼
//@Configuration
//@ConfigurationProperties(prefix = "app.bucket")
//class ImageManagerProperties {
//    lateinit var bucketType: String
//
//    fun getBucketTypeEnum(): ManagerType = ManagerType.valueOf(bucketType)
//
//    private val validNameRegex = Regex("[a-zA-Z-_0-9]*")
//    var bucketName: String = ""
//        get() {
//            if (field.isEmpty() || validNameRegex.matchEntire(field) == null)
//                throw IllegalArgumentException("bucketName is required when UploaderType.ORACLE")
//            return field
//        }
//}

// 새로 생긴거 테스트
@Configuration
@ConfigurationProperties(prefix = "app.bucket")
data class ImageManagerProperties(
    val localDirectory: String = "C:/images"  // 예시: 로컬 디렉토리 경로 설정
)
