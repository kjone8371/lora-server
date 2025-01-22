package com.dipvision.lora.business.facility.service

import com.dipvision.lora.business.facility.dto.*
import com.dipvision.lora.common.page.PageRequest
import com.dipvision.lora.common.page.SlicedResponse
import org.springframework.web.multipart.MultipartFile

interface FacilityService {
    fun findFacilitiesByGeo(lat: Double, lon: Double, distance: Double): List<FacilityDto>

//    fun findFacilitiesByGeo(lat: Double, lng: Double): List<FacilityDto>
    fun findFacilitiesByName(name: String): List<FacilityDto>
//    fun findFacilitiesByName(name: String, pageable: PageRequest): SlicedResponse<FacilityDto>

    fun findFacilitiesByAddress(address: String, pageable: PageRequest): SlicedResponse<FacilityDto>
    fun findById(id: Long): FacilityDto

    fun createFacility(facilityCreateDto: FacilityCreateDto, multipartFile: MultipartFile?): FacilityDto
    fun editFacility(id: Long, facilityEditDto: FacilityEditDto, multipartFile: MultipartFile?): FacilityDto
    fun deleteFacility(id: Long)

    fun setupFacilityRemote(id: Long, dto: FacilityRemoteInfoCreateDto): FacilityRemoteInfoDto
    fun getFacilityRemote(id: Long): FacilityRemoteInfoDto

    fun toggleFacilityLight(id: Long, dto: FacilityLightToggleDto)
     fun elasticRefresh()
}