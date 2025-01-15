package com.dipvision.lora.core.facility.repository

import com.dipvision.lora.core.facility.entity.Facility
import com.dipvision.lora.core.facility.entity.FacilityInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FacilityJpaRepository : JpaRepository<Facility, Long> {
    fun findFacilitiesByIdIn(ids: List<Long>): List<Facility>
}