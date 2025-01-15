package com.dipvision.lora.business.facility.service

import com.dipvision.lora.business.facility.dto.*
import com.dipvision.lora.common.page.PageRequest
import com.dipvision.lora.common.page.SlicedResponse
import org.springframework.web.multipart.MultipartFile

interface FacilityService {
    fun findFacilitiesByGeo(lat: Double, lon: Double, distance: Double): List<FacilityDto>
    fun findFacilitiesByName(name: String, pageable: PageRequest): SlicedResponse<FacilityDto>
    fun findFacilitiesByAddress(address: String, page: Int, size: Int): SlicedResponse<FacilityDto>
    fun findById(id: Long): FacilityDto

    fun createFacility(facilityCreateDto: FacilityCreateDto): FacilityDto
    fun editFacility(id: Long, facilityEditDto: FacilityEditDto): FacilityDto
    fun deleteFacility(id: Long)

    fun createFacilityInfo(facilityInfoCreateDto: FacilityInfoCreateDto): FacilityInfoDto
    fun findByInfoId(id: Long): FacilityInfoDto

}