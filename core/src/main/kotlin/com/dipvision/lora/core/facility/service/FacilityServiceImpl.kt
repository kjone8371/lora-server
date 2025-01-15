package com.dipvision.lora.core.facility.service

import com.dipvision.lora.business.facility.dto.*
import com.dipvision.lora.business.facility.service.FacilityService
import com.dipvision.lora.common.page.SlicedResponse
import com.dipvision.lora.common.permission.Permissions
import com.dipvision.lora.core.common.pageable.ElasticPageRequest
import com.dipvision.lora.core.common.permission.validate
import com.dipvision.lora.core.facility.entity.Facility
import com.dipvision.lora.core.facility.entity.FacilityDocument
import com.dipvision.lora.business.facility.consts.FacilityStatus
import com.dipvision.lora.business.facility.consts.FacilityType
import com.dipvision.lora.common.page.PageRequest
import com.dipvision.lora.core.facility.entity.FacilityInfo
import com.dipvision.lora.core.facility.findSafe
import com.dipvision.lora.core.facility.image.Image
import com.dipvision.lora.core.facility.repository.FacilityInfoJpaRepository
import com.dipvision.lora.core.facility.repository.FacilityJpaRepository
import com.dipvision.lora.core.facility.repository.FacilitySearchRepository
import com.dipvision.lora.core.facility.repository.ImageRepository
import com.dipvision.lora.core.facility.toDto
import com.dipvision.lora.core.member.MemberHolder
import org.aspectj.weaver.tools.cache.SimpleCacheFactory.path
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Service
@Transactional(readOnly = true, rollbackFor = [Exception::class])
class FacilityServiceImpl(
    private val facilityJpaRepository: FacilityJpaRepository,
    private val facilitySearchRepository: FacilitySearchRepository,
    private val facilityInfoJpaRepository: FacilityInfoJpaRepository,
    private val imageRepository: ImageRepository,

    private val memberHolder: MemberHolder,
) : FacilityService {
    override fun findFacilitiesByGeo(lat: Double, lon: Double, distance: Double): List<FacilityDto> {
        memberHolder.getUserPermission().validate(Permissions.READ_FACILITY)

        val ids = facilitySearchRepository.findByPoint(lat, lon, distance).map { it.id }
        val found = facilityJpaRepository.findFacilitiesByIdIn(ids)

        return found.map { it.toDto() }
    }

    override fun findFacilitiesByName(
        name: String,
        pageable: PageRequest,
    ): SlicedResponse<FacilityDto> {
        memberHolder.getUserPermission().validate(Permissions.READ_FACILITY)

        val slice = facilitySearchRepository.findByName(name, ElasticPageRequest(pageable.size, pageable.page))
        val found = facilityJpaRepository.findFacilitiesByIdIn(slice.content.map { it.id })

        return SlicedResponse(slice.hasNext(), slice.size, found.map { it.toDto() })
    }

    override fun findFacilitiesByAddress(address: String, page: Int, size: Int): SlicedResponse<FacilityDto> {
        memberHolder.getUserPermission().validate(Permissions.READ_FACILITY)

        val slice = facilitySearchRepository.findByAddress(address, ElasticPageRequest(page, size))
        val found = facilityJpaRepository.findFacilitiesByIdIn(slice.data.map { it.id })

        return SlicedResponse(slice.hasNext, slice.size, found.map { it.toDto() })
    }

    override fun findById(id: Long): FacilityDto {
        memberHolder.getUserPermission().validate(Permissions.READ_FACILITY)

        val facility = facilityJpaRepository.findSafe(id)
        return facility.toDto()
    }


//    @Transactional(rollbackFor = [Exception::class])
//    override fun createFacility(facilityCreateDto: FacilityCreateDto): FacilityDto {
//        val (
//            name: String,
//            type: FacilityType,
//            status: FacilityStatus,
//            address: String,
//            latitude: Double,
//            longitude: Double,
//            meterNumber: String,
//            department: String,
//            fixture: String,
//            poleFormat: String,
//            dimmer: String,
//
//
//
//        ) = facilityCreateDto
//
//        memberHolder.getUserPermission().validate(Permissions.CREATE_FACILITY)
//
//
//        val facility = facilityJpaRepository.save(
//            Facility(
//                name,
//                type,
//                status,
//                address,
//                latitude,
//                longitude,
//                meterNumber,
//                department,
//                fixture,
//                poleFormat,
//                dimmer
//            )
//        )
//
//        facilitySearchRepository.save(FacilityDocument.fromEntity(facility))
//
//        return facility.toDto()
//    }


    @Transactional(rollbackFor = [Exception::class])
    override fun createFacility(facilityCreateDto: FacilityCreateDto): FacilityDto {
        val (
            name: String,
            type: FacilityType,
            status: FacilityStatus,
            address: String,
            latitude: Double,
            longitude: Double,
            meterNumber: String,
            department: String,
            fixture: String,
            poleFormat: String,
            dimmer: String,
            imageFilename: String,  // 이미지 파일명
            memo: String,
        ) = facilityCreateDto

        // 사용자 권한 확인
        memberHolder.getUserPermission().validate(Permissions.CREATE_FACILITY)

        // 이미지 저장 또는 업데이트
        val image = imageRepository.findByFilename(imageFilename)
            ?: Image(filename = imageFilename)  // 이미지가 없다면 새로 생성

        imageRepository.save(image)  // 새로 생성되거나 기존 이미지가 저장됨

        // Facility 엔티티 생성
        val facility = Facility(
            name,
            type,
            status,
            address,
            latitude,
            longitude,
            meterNumber,
            department,
            fixture,
            poleFormat,
            dimmer,
            image, // 이미지 엔티티를 Facility에 설정
            memo
            )

        // Facility 엔티티 저장
        val savedFacility = facilityJpaRepository.save(facility)

        // Facility를 검색 인덱스에 저장
        facilitySearchRepository.save(FacilityDocument.fromEntity(savedFacility))

        // Facility를 DTO로 변환하여 반환
        return savedFacility.toDto()
    }


    @Transactional(rollbackFor = [Exception::class])
    override fun editFacility(id: Long, facilityEditDto: FacilityEditDto): FacilityDto {
        // 'id'가 null일 경우 예외 처리 추가
        if (id == null) {
            throw IllegalArgumentException("Facility ID is required")
        }

        // 권한 검증
        memberHolder.getUserPermission().validate(Permissions.READ_FACILITY)

        // facility 조회
        val facility = facilityJpaRepository.findSafe(id)

        val (
            nameOrNull: String?,
            typeOrNull: FacilityType?,
            statusOrNull: FacilityStatus?,
            addressOrNull: String?,
            latitudeOrNull: Double?,
            longitudeOrNull: Double?,
            meterNumberOrNull: String?,
            departmentOrNull: String?,
            fixtureOrNull: String?,
            poleFormatOrNull: String?,
            dimmerOrNull: String?,
            imageFilenameOrNull: String?, // 이미지 파일명
            memoOrNull: String?
        ) = facilityEditDto

        memberHolder.getUserPermission().validate(Permissions.UPDATE_FACILITY)

        // 이미지 업데이트 (이미지 정보가 있다면 처리)
        if (imageFilenameOrNull != null) {
            // 기존 이미지 파일명으로 이미지를 찾아서 업데이트 또는 새로 저장
            val image = imageRepository.findByFilename(imageFilenameOrNull)
                ?.apply {
                    path = "/images/uploads/$imageFilenameOrNull" // 적절한 경로로 수정
                }
                ?: Image(filename = imageFilenameOrNull)

            imageRepository.save(image)  // 이미지 저장 또는 업데이트
            facility.image = image // Facility에 이미지 할당
        }

        // 시설 정보 업데이트
        val saved = facilityJpaRepository.save(
            facility.apply {
                name = nameOrNull ?: facility.name
                type = typeOrNull ?: facility.type
                status = statusOrNull ?: facility.status
                address = addressOrNull ?: facility.address
                latitude = latitudeOrNull ?: facility.latitude
                longitude = longitudeOrNull ?: facility.longitude
                meterNumber = meterNumberOrNull ?: facility.meterNumber
                department = departmentOrNull ?: facility.department
                fixture = fixtureOrNull ?: facility.fixture
                poleFormat = poleFormatOrNull ?: facility.poleFormat
                dimmer = dimmerOrNull ?: facility.dimmer
                memo = memoOrNull ?: facility.memo
            }
        )

        // Facility를 검색 인덱스에 저장
        facilitySearchRepository.save(FacilityDocument.fromEntity(saved))

        return saved.toDto()
    }




//    @Transactional(rollbackFor = [Exception::class])
//    override fun editFacility(id: Long, facilityEditDto: FacilityEditDto): FacilityDto {
//        memberHolder.getUserPermission().validate(Permissions.READ_FACILITY)
//        val facility = facilityJpaRepository.findSafe(id)
//
//        val (
//            nameOrNull: String?,
//            typeOrNull: FacilityType?,
//            statusOrNull: FacilityStatus?,
//            addressOrNull: String?,
//            latitudeOrNull: Double?,
//            longitudeOrNull: Double?,
//            meterNumberOrNull: String?,
//            departmentOrNull: String?,
//            fixtureOrNull: String?,
//            poleFormatOrNull: String?,
//            dimmerOrNull: String?
//        ) = facilityEditDto
//
//        memberHolder.getUserPermission().validate(Permissions.UPDATE_FACILITY)
//        val saved = facilityJpaRepository.save(
//            facility.apply {
//                name = nameOrNull ?: facility.name
//                type = typeOrNull ?: facility.type
//                status = statusOrNull ?: facility.status
//
//                address = addressOrNull ?: facility.address
//                latitude = latitudeOrNull ?: facility.latitude
//                longitude = longitudeOrNull ?: facility.longitude
//                meterNumber = meterNumberOrNull ?: facility.meterNumber
//                department = departmentOrNull ?: facility.department
//                fixture = fixtureOrNull ?: facility.fixture
//                poleFormat = poleFormatOrNull ?: facility.poleFormat
//                dimmer = dimmerOrNull ?: facility.dimmer
//
//            }
//        )
//        facilitySearchRepository.save(FacilityDocument.fromEntity(saved))
//
//        return saved.toDto()
//    }

    override fun deleteFacility(id: Long) {
        memberHolder.getUserPermission().validate(Permissions.READ_FACILITY)
        val facility = facilityJpaRepository.findSafe(id)

        memberHolder.getUserPermission().validate(Permissions.DELETE_FACILITY)
        facilityJpaRepository.delete(facility)
        facilitySearchRepository.delete(FacilityDocument.fromEntity(facility))
    }


    //시설 상세 정보 넣기
    @Transactional(rollbackFor = [Exception::class])
    override fun createFacilityInfo(facilityInfoCreateDto: FacilityInfoCreateDto): FacilityInfoDto {
        val (
            lightingType,
            memo,
            image,
            fixture,
            poleFormat,
            poleNumber,
            department,
            streetAddress,
            meterNumber,
            dimmer,
            facilityId
        ) = facilityInfoCreateDto

        // 권한 검증
        memberHolder.getUserPermission().validate(Permissions.CREATE_FACILITY)

        // FacilityId로 해당 Facility를 가져옴 (findById 사용)
        val facility = facilityJpaRepository.findById(facilityId).orElseThrow {
            IllegalArgumentException("Facility not found with id $facilityId")
        }

        // FacilityInfo 엔티티 생성
        val facilityInfo = FacilityInfo(
            lightingType = lightingType,
            memo = memo,
            image = image,
            fixture = fixture,
            poleFormat = poleFormat,
            poleNumber = poleNumber,
            department = department,
            streetAddress = streetAddress,
            meterNumber = meterNumber,
            dimmer = dimmer,
            facility = facility
        )

        // FacilityInfo 저장
        val savedFacilityInfo = facilityInfoJpaRepository.save(facilityInfo)

        // FacilityInfo DTO로 변환하여 반환
        return FacilityInfoDto(
            id = savedFacilityInfo.id!!,
            lightingType = savedFacilityInfo.lightingType ?: "",
            memo = savedFacilityInfo.memo ?: "",
            image = savedFacilityInfo.image ?: "",
            fixture = savedFacilityInfo.fixture ?: "",
            poleFormat = savedFacilityInfo.poleFormat ?: "",
            poleNumber = savedFacilityInfo.poleNumber ?: "",
            department = savedFacilityInfo.department ?: "",
            streetAddress = savedFacilityInfo.streetAddress ?: "",
            meterNumber = savedFacilityInfo.meterNumber ?: "",
            dimmer = savedFacilityInfo.dimmer ?: ""
        )
    }

    override fun findByInfoId(id: Long): FacilityInfoDto {
        memberHolder.getUserPermission().validate(Permissions.READ_FACILITY)

        // FacilityInfo 엔티티를 findSafe로 조회
        val facilityInfo = facilityInfoJpaRepository.findById(id).orElseThrow {
            IllegalArgumentException("FacilityInfo not found with id $id")
        }

        // FacilityInfo 엔티티를 FacilityInfoDto로 변환하여 반환
        return facilityInfo.toDto()
    }



}