package com.dipvision.lora.api.facility.controller

import com.dipvision.lora.api.facility.dto.FacilityCreateRequest
import com.dipvision.lora.api.facility.dto.FacilityEditRequest
import com.dipvision.lora.api.facility.dto.FacilityInfoCreateRequest
import com.dipvision.lora.business.facility.dto.*
import com.dipvision.lora.business.facility.service.FacilityService
import com.dipvision.lora.business.remote.dto.RemoteDto
import com.dipvision.lora.common.exception.CustomException
import com.dipvision.lora.common.page.PageRequest
import com.dipvision.lora.common.response.ResponseData
import com.dipvision.lora.common.response.ResponseEmpty
import dev.jombi.blog.common.multipart.ValidImageFile
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.multipart.MultipartFile


@RestController
//@CrossOrigin(origins = ["https://4b78-218-233-244-111.ngrok-free.app", "https://the-one-led.vercel.app"])
@RequestMapping("/facilities")
@SecurityRequirement(name = "Authorization")
class FacilityController(
    private val facilityService: FacilityService,


) {

//     시설 경도 위도 검색
//    @GetMapping("/search/{lat}/{lng}")
//    @CrossOrigin(origins = ["https://ec65-218-233-244-111.ngrok-free.app/facilities/search/{lat}/{lng}", "https://the-one-led.vercel.app/facilities/search/{lat}/{lng}"])
//    fun findFacilitiesByGeo(
//        @PathVariable lat: Double,
//        @PathVariable lng: Double,
//        @RequestParam(required = false) distance: Double?,
//    ) = facilityService.findFacilitiesByGeo(lat, lng, distance ?: 2.0)


    // 일단 보류 (경도 위도)
//    @CrossOrigin(origins = ["http://localhost:3000", "https://4b78-218-233-244-111.ngrok-free.app/facilities/search/{lat}/{lng}", "https://the-one-led.vercel.app/facilities/search/{lat}/{lng}"])
    @GetMapping("/search/{lat}/{lng}")
    fun findFacilitiesByGeo(
        @PathVariable lat: Double,
        @PathVariable lng: Double
    ): ResponseEntity<List<FacilityDto>> {
        return try {
            val facilities: List<FacilityDto> = facilityService.findFacilitiesByGeo(lat, lng, 1000.0)
            if (facilities.isNotEmpty()) {
                ResponseEntity.ok(facilities)
            } else {
                ResponseEntity.status(HttpStatus.NO_CONTENT).body(emptyList())
            }
        } catch (e: IllegalArgumentException) {
            // 잘못된 매개변수 처리
            println("Invalid argument: ${e.message}")
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(emptyList())
        } catch (e: IllegalStateException) {
            // 비정상 상태 처리
            println("Illegal state: ${e.message}")
            ResponseEntity.status(HttpStatus.CONFLICT).body(emptyList())
        } catch (e: Exception) {
            // 기타 예외 처리
            println("Unexpected error: ${e.message}")
            e.printStackTrace() // 스택 트레이스 출력
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(emptyList())
        }
    }



    // /facilities/search/{lat}/{lng}/{distance}로 요청을 받아 특정 거리 내의 시설을 검색
//    @CrossOrigin(origins = ["http://localhost:3000", "https://ec65-218-233-244-111.ngrok-free.app/facilities/search/{lat}/{lng}", "https://the-one-led.vercel.app/facilities/search/{lat}/{lng}"])
//    @GetMapping("/search/{lat}/{lng}")
//    fun findFacilitiesByGeo(
//        @PathVariable lat: Double,
//        @PathVariable lng: Double
//    ): ResponseEntity<List<FacilityDto>> { // List<FacilityDto> 반환
//        return try {
//            // 특정 거리 내의 시설들을 찾기 위해 1000m (1km) 반경으로 설정
//            val facilities: List<FacilityDto> = facilityService.findFacilitiesByGeo(lat, lng, 200.0)
//            // List<FacilityDto>를 그대로 반환
//            if (facilities.isNotEmpty()) {
//                ResponseEntity.ok(facilities)
//            } else {
//                ResponseEntity.status(HttpStatus.NO_CONTENT).body(emptyList())
//            }
//        } catch (e: Exception) {
//            // 예외 발생 시 500 내부 서버 오류 응답
//            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(emptyList())
//        }
//    }

//    private val logger: Logger = LoggerFactory.getLogger(FacilityController::class.java)
//
//    @GetMapping("/search/{lat}/{lng}")
//    @CrossOrigin(origins = ["http://localhost:3000", "https://ec65-218-233-244-111.ngrok-free.app"])
//    fun findFacilitiesByGeo(
//        @PathVariable lat: Double,
//        @PathVariable lng: Double,
//        @RequestParam(required = false) distance: Double?,
//    ): ResponseEntity<Any> {
//        return try {
//            // 정상적인 처리 로직
//            val facilities = facilityService.findFacilitiesByGeo(lat, lng, distance ?: 2.0)
//            ResponseEntity.ok(facilities)
//        } catch (e: Exception) {
//            // 예외 발생 시 로그 남기고, 500 내부 서버 오류 응답
//            logger.error("Error while processing request for latitude: $lat, longitude: $lng, distance: $distance", e)
//            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: ${e.message}")
//        }
//    }

    @GetMapping("/search/address")
    fun findFacilitiesByAddress(
        @RequestParam address: String,
        pageRequest: PageRequest,
    ) = facilityService.findFacilitiesByAddress(address, pageRequest)

//    @GetMapping("/search/name")
//    fun findFacilitiesByName(
//        @RequestParam name: String,
//        pageRequest: PageRequest,
//    ) = facilityService.findFacilitiesByName(name, pageRequest)

    @GetMapping("/search/name")
    fun finFacilitiesByName(
        @RequestParam name: String
    ) = facilityService.findFacilitiesByName(name)


    // 시설 조회
    @GetMapping("/{id}")
    fun findFacilitiesByName(@PathVariable id: Long) = facilityService.findById(id)


    // 시설 설치
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createFacility(
        @Valid
        @RequestPart("data", required = true)
        request: FacilityCreateRequest,

        @Valid
        @ValidImageFile([MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE], 10L * 1024L * 1024L)
        @RequestPart(name = "image", required = false)
        multipartFile: MultipartFile?,
    ): ResponseEntity<ResponseData<FacilityDto>> {
        val dto = facilityService.createFacility(
            FacilityCreateDto(
                request.name,
                request.type,
                request.status,

                request.address,
                request.latitude,
                request.longitude,

                request.department,
                request.fixture,
                request.poleFormat,
                request.dimmer,

                request.memo,
                request.phoneNumber,
                request.escoStatus,
                request.powerConsumption,
                request.billingType,
            ),
            multipartFile
        )

        return ResponseData.ok(data = dto)

    }


    @PatchMapping("/{id}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun editFacility(
        @PathVariable
        id: Long,

        @Valid
        @RequestPart("data", required = true)
        request: FacilityEditRequest,

        @Valid
        @ValidImageFile([MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE], 10L * 1024L * 1024L)
        @RequestPart(name = "image", required = false)

        multipartFile: MultipartFile?,
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

                request.department,
                request.fixture,
                request.poleFormat,
                request.dimmer,
                request.memo,
                request.phoneNumber,
                request.escoStatus,
                request.powerConsumption,
                request.billingType,
            ),
            multipartFile
        )

        return ResponseData.ok(data = dto)
    }

    @DeleteMapping("/{id}")
    fun deleteFacility(@PathVariable id: Long): ResponseEntity<ResponseEmpty> {
        facilityService.deleteFacility(id)
        return ResponseEmpty.noContent()
    }



    // 시설 통신 원격 조정
    @PostMapping("/{id}/remote")
    fun createOrEditFacilityRemote(
        @PathVariable id: Long,
        @RequestBody request: FacilityRemoteInfoCreateDto,
    ): ResponseEntity<ResponseData<FacilityRemoteInfoDto>> {
        val info = facilityService.setupFacilityRemote(id, request)
        return ResponseData.ok(data = info)
    }

    // 11 자리 -> 계기 번호(010100202-11) varchar(12)

    // 시설 통신 아이디 조회
    @GetMapping("/{id}/remote")
    fun createOrEditFacilityRemote(
        @PathVariable id: Long,
    ): ResponseEntity<ResponseData<FacilityRemoteInfoDto>> {
        val info = facilityService.getFacilityRemote(id)
        return ResponseData.ok(data = info)
    }

    // 불 토글 설정
    @PatchMapping("/{id}/remote/light")
    fun createOrEditFacilityRemote(
        @PathVariable id: Long,
        @RequestBody request: FacilityLightToggleDto,
    ): ResponseEntity<ResponseEmpty> {
        facilityService.toggleFacilityLight(id, request)
        return ResponseEmpty.noContent()
    }

    @PostMapping("/elastic-refresh")
    fun elasticRefresh(): ResponseEntity<ResponseEmpty> {
        facilityService.elasticRefresh()
        return ResponseEmpty.noContent()
    }

//    // 클라이언트 연결 상태 확인
//    @GetMapping("/{id}/status")
//    fun getClientStatus(@PathVariable id: Long): String {
//        val isConnected = mqttPool.isClientConnected(id)
//        return if (isConnected) {
//            "Client $id is connected."
//        } else {
//            "Client $id is not connected."
//        }
//    }


}