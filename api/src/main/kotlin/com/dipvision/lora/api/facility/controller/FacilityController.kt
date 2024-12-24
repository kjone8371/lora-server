package com.dipvision.lora.api.facility.controller

import com.dipvision.lora.api.facility.dto.FacilityCreateRequest
import com.dipvision.lora.api.facility.dto.FacilityEditRequest
import com.dipvision.lora.business.facility.dto.FacilityCreateDto
import com.dipvision.lora.business.facility.dto.FacilityDto
import com.dipvision.lora.business.facility.dto.FacilityEditDto
import com.dipvision.lora.business.facility.service.FacilityService
import com.dipvision.lora.common.page.PageRequest
import com.dipvision.lora.common.response.ResponseData
import com.dipvision.lora.common.response.ResponseEmpty
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/facilities")
@SecurityRequirement(name = "Authorization")
class FacilityController(
    private val facilityService: FacilityService,
) {
    @GetMapping("/search/{lat}/{lng}")
    fun findFacilitiesByGeo(
        @PathVariable lat: Double,
        @PathVariable lng: Double,
        @RequestParam(required = false) distance: Double?,
    ) = facilityService.findFacilitiesByGeo(lat, lng, distance ?: 2.0)

    @GetMapping("/search/address")
    fun findFacilitiesByAddress(
        @RequestParam address: String,
        pageRequest: PageRequest,
    ) = facilityService.findFacilitiesByAddress(address, pageRequest.page, pageRequest.size)

    @GetMapping("/search/name")
    fun findFacilitiesByName(
        @RequestParam name: String,
        pageRequest: PageRequest,
    ) = facilityService.findFacilitiesByName(name, pageRequest)

    @GetMapping("/{id}")
    fun findFacilitiesByName(@PathVariable id: Long) = facilityService.findById(id)

    @PostMapping
    fun createFacility(@Valid @RequestBody request: FacilityCreateRequest): ResponseEntity<ResponseData<FacilityDto>> {
        val dto = facilityService.createFacility(
            FacilityCreateDto(
                request.name,
                request.type,
                request.status,

                request.address,
                request.latitude,
                request.longitude
            )
        )

        return ResponseData.ok(data = dto)
    }

    @PatchMapping("/{id}")
    fun editFacility(
        @PathVariable id: Long,
        @Valid @RequestBody request: FacilityEditRequest,
    ): ResponseEntity<ResponseData<FacilityDto>> {
        val dto = facilityService.editFacility(
            id,
            FacilityEditDto(
                request.name,
                request.type,
                request.status,

                request.address,
                request.latitude,
                request.longitude
            )
        )

        return ResponseData.ok(data = dto)
    }

    @DeleteMapping("/{id}")
    fun deleteFacility(@PathVariable id: Long): ResponseEntity<ResponseEmpty> {
        facilityService.deleteFacility(id)
        return ResponseEmpty.noContent()
    }
}