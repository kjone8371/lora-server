package com.dipvision.lora.api.facility.controller

import com.dipvision.lora.api.facility.dto.FacilityCreateRequest
import com.dipvision.lora.api.facility.dto.FacilityEditRequest
import com.dipvision.lora.api.facility.dto.FacilityInfoCreateRequest
import com.dipvision.lora.business.facility.dto.*
import com.dipvision.lora.business.facility.service.FacilityService
import com.dipvision.lora.common.page.PageRequest
import com.dipvision.lora.common.response.ResponseData
import com.dipvision.lora.common.response.ResponseEmpty
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springdoc.webmvc.core.service.RequestService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["https://1e5a-218-233-244-111.ngrok-free.app", "https://the-one-led.vercel.app"])
@RequestMapping("/facilities")
@SecurityRequirement(name = "Authorization")
class FacilityController(
    private val facilityService: FacilityService,
    private val requestService: RequestService,
) {
    @GetMapping("/search/{lat}/{lng}")
    @CrossOrigin(origins = ["https://1e5a-218-233-244-111.ngrok-free.app/facilities/search/{lat}/{lng}", "https://the-one-led.vercel.app/facilities/search/{lat}/{lng}"])
    fun findFacilitiesByGeo(
        @PathVariable lat: Double,
        @PathVariable lng: Double,
        @RequestParam(required = false) distance: Double?,
    ) = facilityService.findFacilitiesByGeo(lat, lng, distance ?: 2.0)

    @GetMapping("/search/address")
    @CrossOrigin(origins = ["https://1e5a-218-233-244-111.ngrok-free.app/facilities/search/address", "https://the-one-led.vercel.app/facilities/search/address"])
    fun findFacilitiesByAddress(
        @RequestParam address: String,
        pageRequest: PageRequest,
    ) = facilityService.findFacilitiesByAddress(address, pageRequest.page, pageRequest.size)

    @GetMapping("/search/name")
    @CrossOrigin(origins = ["https://1e5a-218-233-244-111.ngrok-free.app/facilities/search/name", "https://the-one-led.vercel.app/facilities/search/name"])
    fun findFacilitiesByName(
        @RequestParam name: String,
        pageRequest: PageRequest,
    ) = facilityService.findFacilitiesByName(name, pageRequest)

    // 시설 조회
    @GetMapping("/{id}")
    @CrossOrigin(origins = ["https://1e5a-218-233-244-111.ngrok-free.app/facilities/{id}", "https://the-one-led.vercel.app/facilities/{id}"])
    fun findFacilitiesByName(@PathVariable id: Long) = facilityService.findById(id)

    // 시설 상세 정보
    @GetMapping("/facility/info/{id}")
    @CrossOrigin(origins = ["\"https://1e5a-218-233-244-111.ngrok-free.app/facilities/facility/info/{id}", "https://the-one-led.vercel.app/facilities/facility/info/{id}"])
    fun findByInfoId(@PathVariable id: Long) = facilityService.findByInfoId(id)


    //시설 상세 정보 데이터 넣기
    @PostMapping("/facility/info/{id}")
    @CrossOrigin(origins = ["https://1e5a-218-233-244-111.ngrok-free.app/facilities/facility/info/{id}", "https://the-one-led.vercel.app/facilities/facility/info/{id}"])
    fun createFacilityInfo(
        @PathVariable id: Long,  // 경로에서 facilityId를 받음
        @Valid @RequestBody request: FacilityInfoCreateRequest  // 본문에서 나머지 데이터를 받음
    ): ResponseEntity<ResponseData<FacilityInfoDto>> {

        // DTO로 변환하여 Service 호출
        val dto = facilityService.createFacilityInfo(
            FacilityInfoCreateDto(
                request.lightingType,
                request.memo,
                request.image,
                request.fixture,
                request.poleFormat,
                request.poleNumber,
                request.department,
                request.streetAddress,
                request.meterNumber,
                request.dimmer,
                facilityId = id  // 경로 변수에서 받은 id 사용
            )
        )

        // 응답을 ResponseData에 담아 반환
        return ResponseData.ok(data = dto)
    }



    // 시설 설치
    @PostMapping
    @CrossOrigin(origins = ["https://1e5a-218-233-244-111.ngrok-free.app/facilities", "https://the-one-led.vercel.app/facilities"])
    fun createFacility(@Valid @RequestBody request: FacilityCreateRequest): ResponseEntity<ResponseData<FacilityDto>> {
        val dto = facilityService.createFacility(
            FacilityCreateDto(
                request.name,
                request.type,
                request.status,

                request.address,
                request.latitude,
                request.longitude,

                request.meterNumber,
                request.department,
                request.fixture,
                request.poleFormat,
                request.dimmer,

                request.imageFilename,
                request.memo

            )
        )

        return ResponseData.ok(data = dto)
    }

    @PatchMapping("/{id}")
    @CrossOrigin(origins = ["https://1e5a-218-233-244-111.ngrok-free.app/facility/{id}", "https://the-one-led.vercel.app/facility/{id}"])
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
                request.longitude,

                request.meterNumber,
                request.department,
                request.fixture,
                request.poleFormat,
                request.dimmer,

                request.imageFilename,
                request.memo
            )
        )

        return ResponseData.ok(data = dto)
    }

    @DeleteMapping("/{id}")
    @CrossOrigin(origins = ["https://1e5a-218-233-244-111.ngrok-free.app/facility/{id}", "https://the-one-led.vercel.app/facility/{id}"])
    fun deleteFacility(@PathVariable id: Long): ResponseEntity<ResponseEmpty> {
        facilityService.deleteFacility(id)
        return ResponseEmpty.noContent()
    }
}