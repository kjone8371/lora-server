package com.dipvision.lora.core.facility.repository

import com.dipvision.lora.core.facility.entity.Facility
import jakarta.persistence.LockModeType
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.awt.print.Pageable

@Repository
interface FacilityJpaRepository : JpaRepository<Facility, Long> {
    fun findFacilitiesByIdIn(ids: List<Long>): List<Facility>

    fun findByName(name: String): List<Facility>

    @Query("SELECT f FROM Facility f WHERE " +
            "ST_Distance(POINT(:lon, :lat), POINT(f.longitude, f.latitude)) <= :distance")
    fun findFacilitiesNearby(@Param("lat") lat: Double, @Param("lon") lon: Double, @Param("distance") distance: Double): List<Facility>
}