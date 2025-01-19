package com.dipvision.lora.infra.musicismylife

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter
import org.springframework.lang.Nullable
import org.springframework.stereotype.Component
import java.lang.reflect.Type


// 새로 생긴거
@Component
class MultipartJackson2HttpMessageConverter(
    objectMapper: ObjectMapper
) : AbstractJackson2HttpMessageConverter(objectMapper, MediaType.APPLICATION_OCTET_STREAM) {

    /**
     * HTTP 요청 데이터의 처리를 위한 메시지 변환기
     * "Content-Type: multipart/form-data" 헤더를 지원.
     */
    override fun canWrite(clazz: Class<*>, mediaType: MediaType?): Boolean {
        return false
    }

    override fun canWrite(@Nullable type: Type?, clazz: Class<*>, @Nullable mediaType: MediaType?): Boolean {
        return false
    }

    override fun canWrite(mediaType: MediaType?): Boolean {
        return false
    }
}