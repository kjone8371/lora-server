package com.dipvision.lora.core.facility.repository

import com.dipvision.lora.business.facility.dto.FacilityDto
import com.dipvision.lora.core.facility.entity.Facility
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface FacilityJpaRepository : JpaRepository<Facility, Long> {
    fun findFacilitiesByIdIn(ids: List<Long>): List<Facility>

    // name을 기준으로 Facility를 조회하는 메서드 추가
    fun findFacilitiesByNameContainingIgnoreCase(name: String): List<Facility>

    // 새로 추가한 것
    fun findByLatitudeAndLongitude(latitude: Double, longitude: Double): List<Facility>

    // 비관적 잠금: 이 메서드는 Facility 엔티티를 비관적으로 잠급니다.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT f FROM Facility f WHERE f.id = :id")
    fun lockFacility(id: Long): Facility

}