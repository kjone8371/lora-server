package com.dipvision.lora.core.facility.repository

import com.dipvision.lora.core.facility.entity.FacilityInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FacilityInfoJpaRepository : JpaRepository<FacilityInfo, Long> {
    // 추가적인 쿼리 메서드가 필요하다면 여기에 작성
}