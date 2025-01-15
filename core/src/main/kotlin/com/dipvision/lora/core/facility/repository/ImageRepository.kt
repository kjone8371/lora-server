package com.dipvision.lora.core.facility.repository

import com.dipvision.lora.core.facility.entity.FacilityInfo
import com.dipvision.lora.core.facility.image.Image
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageRepository : JpaRepository<Image, Long> {

    fun findByFilename(filename: String): Image?  // 파일명으로 이미지 찾기
}