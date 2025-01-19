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
import com.dipvision.lora.common.exception.CustomException
import com.dipvision.lora.common.page.PageRequest
import com.dipvision.lora.common.protocol.SPacket
import com.dipvision.lora.core.facility.entity.FacilityRemoteInfo
import com.dipvision.lora.core.facility.exception.FacilityExceptionDetails
import com.dipvision.lora.core.facility.findSafe
import com.dipvision.lora.core.facility.repository.FacilityJpaRepository
import com.dipvision.lora.core.facility.repository.FacilityRemoteInfoRepository
import com.dipvision.lora.core.facility.repository.FacilitySearchRepository
import com.dipvision.lora.core.facility.toDto
import com.dipvision.lora.core.image.service.ImageInternalService
import com.dipvision.lora.core.member.MemberHolder
import com.dipvision.lora.core.remote.delegate.MqttConnection
import com.dipvision.lora.core.remote.entity.Remote
import com.dipvision.lora.core.remote.findSafe
import com.dipvision.lora.core.remote.repository.RemoteJpaRepository
import jakarta.persistence.LockModeType
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.Lock
import org.springframework.orm.ObjectOptimisticLockingFailureException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import kotlin.math.*
import org.springframework.retry.annotation.Retryable
import org.springframework.retry.annotation.Backoff
import org.springframework.dao.OptimisticLockingFailureException



@Service
@Transactional(readOnly = true, rollbackFor = [Exception::class])
class FacilityServiceImpl(
    private val facilityJpaRepository: FacilityJpaRepository,
    private val facilitySearchRepository: FacilitySearchRepository,
    private val imageInternalService: ImageInternalService,
    private val facilityRemoteInfoRepository: FacilityRemoteInfoRepository,
    private val remoteJpaRepository: RemoteJpaRepository,

    private val memberHolder: MemberHolder,

    private val mqttConnection: MqttConnection,

    ) : FacilityService {
    // 두 점 사이의 거리 계산
    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val radius = 6371.0 // 지구 반지름

        val latDistance = Math.toRadians(lat2 - lat1)

        val lonDistance = Math.toRadians(lon2 - lon1)

        val a = sin(latDistance / 2) * sin(latDistance / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(lonDistance / 2) * sin(lonDistance / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return radius * c // 킬로미터 단위
    }

    // lat, lng와 주어진 거리 내의 시설들을 찾아 List로 반환
    override fun findFacilitiesByGeo(lat: Double, lon: Double, distance: Double): List<FacilityDto> {

        val allFacilities = facilityJpaRepository.findAll()

        // lat, lon에서 특정 거리 이내의 시설만 필터링
        val nearbyFacilities = allFacilities.filter { facility ->

            val facilityLat = facility.latitude // 위도

            val facilityLng = facility.longitude //경도

            val dist = calculateDistance(lat, lon, facilityLat, facilityLng)
            dist <= distance // 거리 비교
        }

        // 필터링된 시설들을 DTO로 변환하여 반환
        return nearbyFacilities.map { it.toDto() }
    }


    // 원래 전에 있던거
//    override fun findFacilitiesByGeo(lat: Double, lon: Double, distance: Double): List<FacilityDto> {
//        memberHolder.getUserPermission().validate(Permissions.READ_FACILITY)

//        val ids = facilitySearchRepository.findByPoint(lat, lon, distance).map { it.id }
//        val found = facilityJpaRepository.findFacilitiesByIdIn(ids)
//
//        return found.map { it.toDto() }
//    }


    // 이름 검색 원래 있던거
//    override fun findFacilitiesByName(
//        name: String,
//        pageable: PageRequest,
//    ): SlicedResponse<FacilityDto> {
//        memberHolder.getUserPermission().validate(Permissions.READ_FACILITY)
//
//        val slice = facilitySearchRepository.findByName(name, ElasticPageRequest(pageable.size, pageable.page))
//        val found = facilityJpaRepository.findFacilitiesByIdIn(slice.content.map { it.id })
//
//        return SlicedResponse(slice.hasNext(), slice.size, found.map { it.toDto() })
//    }


    // 새로운 이름 검색
    override fun findFacilitiesByName(
        name: String,
        pageable: PageRequest,
    ): SlicedResponse<FacilityDto> {
        memberHolder.getUserPermission().validate(Permissions.READ_FACILITY)

        // ElasticSearch에서 결과 조회 (Slice<FacilityDocument> 반환)
        val slice: Slice<FacilityDocument> = facilitySearchRepository.findByName(name, ElasticPageRequest(pageable.size, pageable.page))

        // ID에 해당하는 시설 정보 조회 (JpaRepository를 통해)
        val found = facilityJpaRepository.findFacilitiesByIdIn(slice.content.map { it.id })

        // SlicedResponse로 변환하여 반환
        return SlicedResponse(
            hasNext = slice.hasNext(),
            size = slice.size,
            data = found.map { it.toDto() }  // Facility -> FacilityDto로 변환
        )
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
    override fun createFacility(facilityCreateDto: FacilityCreateDto, multipartFile: MultipartFile?): FacilityDto {
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
            memo: String,
            phoneNumber: String,
            escoStatus: String,
            powerConsumption: String,
            billingType: String,

        ) = facilityCreateDto

        memberHolder.getUserPermission().validate(Permissions.CREATE_FACILITY)

        val image = multipartFile?.let {
            imageInternalService.saveImage(
                multipartFile.originalFilename ?: multipartFile.name,
                multipartFile.bytes
            )
        }

        val facility = facilityJpaRepository.save(
            Facility(
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

                image,
                memo,
                phoneNumber,
                escoStatus,
                powerConsumption,
                billingType

            )
        )
        facilitySearchRepository.save(FacilityDocument.fromEntity(facility))

        return facility.toDto()
    }


    @Transactional(rollbackFor = [Exception::class])
    override fun editFacility(id: Long, facilityEditDto: FacilityEditDto, multipartFile: MultipartFile?): FacilityDto {
        memberHolder.getUserPermission().validate(Permissions.READ_FACILITY)
        val facility = facilityJpaRepository.findSafe(id)

        val (
            nameOrNull: String?,
            typeOrNull: FacilityType?,
            statusOrNull: FacilityStatus?,

            addressOrNull: String?,
            latitudeOrNull: Double?,
            longitudeOrNull: Double?,
            departmentOrNull: String?,
            fixtureOrNull: String?,
            poleFormatOrNull: String?,
            dimmerOrNull: String?,
            memoOrNull: String?,
            phoneNumberOrNull: String?,
            escoStatusOrNull: String?,
            powerConsumptionOrNull: String?,
            billingTypeOrNull: String?,

        ) = facilityEditDto

        memberHolder.getUserPermission().validate(Permissions.UPDATE_FACILITY)

        val imageOrNull = multipartFile?.let {
            imageInternalService.saveImage(
                multipartFile.originalFilename ?: multipartFile.name,
                multipartFile.bytes
            )
        }
        val saved = facilityJpaRepository.save(
            facility.apply {
                name = nameOrNull ?: facility.name
                type = typeOrNull ?: facility.type
                status = statusOrNull ?: facility.status

                address = addressOrNull ?: facility.address
                latitude = latitudeOrNull ?: facility.latitude
                longitude = longitudeOrNull ?: facility.longitude

                department = departmentOrNull ?: facility.department
                fixture = fixtureOrNull ?: facility.fixture
                poleFormat = poleFormatOrNull ?: facility.poleFormat
                dimmer = dimmerOrNull ?: facility.dimmer

                image = imageOrNull ?: facility.image
                memo = memoOrNull ?: facility.memo
                phoneNumber = phoneNumberOrNull ?: facility.phoneNumber
                escoStatus = escoStatusOrNull ?: facility.escoStatus
                powerConsumption = powerConsumptionOrNull ?: facility.powerConsumption
                billingType = billingTypeOrNull ?: facility.billingType
            }
        )
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

    @Retryable(
        value = [OptimisticLockingFailureException::class],
        maxAttempts = 3,
        backoff = Backoff(delay = 1000)
    )
    @Transactional(rollbackFor = [Exception::class])
    override fun setupFacilityRemote(id: Long, dto: FacilityRemoteInfoCreateDto): FacilityRemoteInfoDto {
        val (
            remoteId: Long,
            phone: String,
            number: Int,
        ) = dto

        memberHolder.getUserPermission().validate(Permissions.READ_FACILITY)
        val facility = facilityJpaRepository.findSafe(id)

        memberHolder.getUserPermission().validate(Permissions.UPDATE_FACILITY)
        val remote = remoteJpaRepository.findSafe(remoteId)

        val info = facilityRemoteInfoRepository.save(
            FacilityRemoteInfo(
                facility = facility,

                remote = remote,
                phone = phone.replace("-", ""),
                number = number,
            )
        )

        mqttConnection.listen(info)

        return info.toDto()
    }


//    override fun setupFacilityRemote(id: Long, dto: FacilityRemoteInfoCreateDto): FacilityRemoteInfoDto {
//        val (
//            remoteId: Long,
//            phone: String,
//            number: Int,
//        ) = dto
//
//        memberHolder.getUserPermission().validate(Permissions.READ_FACILITY)
//        val facility = facilityJpaRepository.findSafe(id)
//
//        memberHolder.getUserPermission().validate(Permissions.UPDATE_FACILITY)
//        val remote = remoteJpaRepository.findSafe(remoteId)
//
//        val info = facilityRemoteInfoRepository.save(
//            FacilityRemoteInfo(
//                id = facility.id,
//                facility = facility,
//
//                remote = remote,
//                phone = phone,
//                number = number,
//            )
//        )
//
//        return info.toDto()
//    }




    override fun getFacilityRemote(id: Long): FacilityRemoteInfoDto {
        memberHolder.getUserPermission().validate(Permissions.READ_FACILITY)
        val facility = facilityJpaRepository.findSafe(id)

        val remoteInfo = facility.remoteInfo
            ?: throw CustomException(FacilityExceptionDetails.NO_REMOTE_INFO)

        return remoteInfo.toDto()
    }

    override fun toggleFacilityLight(id: Long, dto: FacilityLightToggleDto) {
        memberHolder.getUserPermission().validate(Permissions.READ_FACILITY)
        val facility = facilityJpaRepository.findSafe(id)

        memberHolder.getUserPermission().validate(Permissions.TEST_FACILITY)
        val (
            isLightOn: Boolean,
            onTime: Int,
        ) = dto

        if (onTime !in 0..180) {
            throw CustomException(FacilityExceptionDetails.TOGGLE_TIME_NOT_MATCH)
        }

        val remoteInfo = facility.remoteInfo
            ?: throw CustomException(FacilityExceptionDetails.NO_REMOTE_INFO)

        mqttConnection.send(remoteInfo, SPacket.SLightTogglePacket(remoteInfo.number, isLightOn, onTime.toShort()))
    }



}