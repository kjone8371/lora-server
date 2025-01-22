package com.dipvision.lora.infra.image

import com.dipvision.lora.core.image.delegate.ImageManager
import com.dipvision.lora.infra.image.annotation.ConditionalOnServiceType
import com.dipvision.lora.infra.image.impl.LocalImageManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

//// 원래꺼
//@Configuration
//class ImageManagerInjection {
//    @Bean
//    @ConditionalOnServiceType(ManagerType.LOCAL)
//    fun localImageBucket(imageManagerProperties: ImageManagerProperties): ImageManager {
//        return LocalImageManager(imageManagerProperties)
//    }
//}

// 새로 생긴거 테스트
@Configuration
class ImageManagerInjection {


    @Bean
    fun localImageManager(imageManagerProperties: ImageManagerProperties): ImageManager {
        return LocalImageManager(imageManagerProperties.localDirectory)
    }
}

