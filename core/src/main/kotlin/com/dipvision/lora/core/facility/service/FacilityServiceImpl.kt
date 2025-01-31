package com.dipvision.lora.core.facility.service

import com.dipvision.lora.business.facility.dto.*
import com.dipvision.lora.business.facility.service.FacilityService
import com.dipvision.lora.common.page.SlicedResponse
import com.dipvision.lora.core.common.pageable.ElasticPageRequest
import com.dipvision.lora.core.facility.entity.Facility
import com.dipvision.lora.core.facility.entity.FacilityDocument
import com.dipvision.lora.business.facility.consts.FacilityStatus
import com.dipvision.lora.business.facility.consts.FacilityType
import com.dipvision.lora.common.exception.CustomException
import com.dipvision.lora.common.page.PageRequest
import org.springframework.security.core.userdetails.User
import com.dipvision.lora.common.permission.Permissions
import com.dipvision.lora.common.protocol.SPacket
import com.dipvision.lora.core.facility.entity.FacilityRemoteInfo
import com.dipvision.lora.core.facility.entity.FacilityTypeConverter
import com.dipvision.lora.core.facility.exception.FacilityExceptionDetails
import com.dipvision.lora.core.facility.findSafe
import com.dipvision.lora.core.facility.repository.FacilityJpaRepository
import com.dipvision.lora.core.facility.repository.FacilityRemoteInfoRepository
import com.dipvision.lora.core.facility.repository.FacilitySearchRepository
import com.dipvision.lora.core.facility.toDto
import com.dipvision.lora.core.image.service.ImageInternalService
import com.dipvision.lora.core.member.MemberHolder
import com.dipvision.lora.core.remote.delegate.MqttConnection
import com.dipvision.lora.core.remote.findSafe
import com.dipvision.lora.core.remote.repository.RemoteJpaRepository
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import kotlin.math.*
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.io.InputStream


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
//    // 두 점 사이의 거리 계산
//    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
//        val radius = 6371.0 // 지구 반지름
//
//        val latDistance = Math.toRadians(lat2 - lat1)
//
//        val lonDistance = Math.toRadians(lon2 - lon1)
//
//        val a = sin(latDistance / 2) * sin(latDistance / 2) +
//                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
//                sin(lonDistance / 2) * sin(lonDistance / 2)
//
//        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
//
//        return radius * c // 킬로미터 단위
//    }
//
//    // lat, lng와 주어진 거리 내의 시설들을 찾아 List로 반환
//    override fun findFacilitiesByGeo(lat: Double, lon: Double, distance: Double): List<FacilityDto> {
//
//        val allFacilities = facilityJpaRepository.findAll()
//
//        // lat, lon에서 특정 거리 이내의 시설만 필터링
//        val nearbyFacilities = allFacilities.filter { facility ->
//
//            val facilityLat = facility.latitude // 위도
//
//            val facilityLng = facility.longitude //경도
//
//            val dist = calculateDistance(lat, lon, facilityLat, facilityLng)
//            dist <= distance // 거리 비교
//        }
//
//        // 필터링된 시설들을 DTO로 변환하여 반환
//        return nearbyFacilities.map { it.toDto() }
//    }

    // 시설 위치를 기준으로 필터링하는 함수
//    override fun findFacilitiesByGeo(lat: Double, lon: Double, distance: Double, userPermission: Permission): List<FacilityDto> {
//        val allFacilities = facilityJpaRepository.findAll() // 모든 시설을 가져옵니다.
//
//        // lat, lon에서 특정 거리 이내의 시설만 필터링
//        val nearbyFacilities = allFacilities.filter { facility ->
//            val facilityLat = facility.latitude // 시설의 위도
//            val facilityLng = facility.longitude // 시설의 경도
//            val dist = calculateDistance(lat, lon, facilityLat, facilityLng) // 두 점 사이 거리 계산
//
//            // 시설이 속한 광역시 가져오기
//            val facilityRegion = getRegionByFacility(facility)
//
//            // 사용자가 권한이 있는 광역시만 필터링 (권한이 있으면 거리를 체크)
//            dist <= distance && userPermission.hasPermission(facilityRegion.permission)
//        }
//
//        // 필터링된 시설들을 DTO로 변환하여 반환
//        return nearbyFacilities.map { it.toDto() }
//    }

    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371 // 지구 반지름 (단위: km)
        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)
        val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return R * c // 결과는 km 단위
    }

    override fun findFacilitiesByGeo(lat: Double, lon: Double, distance: Double): List<FacilityDto> {

        val requiredPermission = getUserPermission() // 사용자 권한 가져오기

        val allFacilities = facilityJpaRepository.findAll() // 모든 시설을 가져옵니다.

        // lat, lon에서 특정 거리 이내의 시설만 필터링
        val nearbyFacilities = allFacilities.filter { facility ->
            val facilityLat = facility.latitude // 시설의 위도
            val facilityLng = facility.longitude // 시설의 경도
            val dist = calculateDistance(lat, lon, facilityLat, facilityLng) // 두 점 사이 거리 계산

            dist <= distance // 단순 거리 체크
        }

        // 필터링된 시설들 중 해당 지역 권한이 일치하는 시설만 반환
        return nearbyFacilities.filter { facility ->
            val region = getRegionByFacility(facility) // 시설의 지역 권한을 가져옴
            region == requiredPermission // 요구하는 권한과 일치하는지 확인
        }.map { it.toDto() } // DTO로 변환하여 반환
    }

    // 시설이 속한 지역 가져오기
    private fun getRegionByFacility(facility: Facility): Permissions {
        val lat = facility.latitude
        val lon = facility.longitude

        println("Facility lat: $lat, lon: $lon")  // 디버깅 메시지

        return when {
            lat in 37.4..37.6 && lon in 126.7..127.1 -> Permissions.SEOUL
            lat in 35.0..35.3 && lon in 128.0..129.0 -> Permissions.BUSAN
            lat in 35.8..36.0 && lon in 128.5..129.5 -> Permissions.DAEGU
            lat in 37.3..37.5 && lon in 126.5..127.0 -> Permissions.INCHEON
            lat in 35.0..35.5 && lon in 126.0..126.5 -> Permissions.GWANGJU
            lat in 36.0..36.5 && lon in 127.0..127.5 -> Permissions.DAEJEON
            lat in 35.5..36.0 && lon in 129.5..130.0 -> Permissions.ULSAN
            else -> {
                // 특정 범위 밖일 때 예외 대신 알림
                println("Warning: 시설 위치가 정의된 범위 밖에 있습니다. lat: $lat, lon: $lon")
                return Permissions.NOTHING  // 기본 권한 반환
            }
        }
    }

    fun getUserPermission(): Permissions? {
        val authentication = SecurityContextHolder.getContext().authentication
        val authorities = authentication?.authorities ?: emptyList()

        // 첫 번째 권한을 가져옵니다 (여러 권한이 있을 수 있지만, 첫 번째 권한만 사용)
        return authorities.firstOrNull()?.let { authority ->
            Permissions.values().find { it.name == authority.authority }
        }
    }

//    fun getUserPermissionFromAuthorities(): Permissions {
//        val authentication = SecurityContextHolder.getContext().authentication
//        val authorities = authentication?.authorities ?: emptyList()
//
//        // 권한이 하나라도 있을 경우, 첫 번째 권한을 사용
//        val authority = authorities.firstOrNull()?.authority ?: throw IllegalArgumentException("사용자 권한이 없습니다.")
//
//        // 문자열로 된 권한을 Permissions enum으로 변환
//        return Permissions.valueOf(authority)
//    }




    override fun findFacilitiesByName(name: String): List<FacilityDto> {
        // 이름으로 시설 검색
        val facilities = facilityJpaRepository.findByName(name)

        // Facility 엔티티를 FacilityDto로 변환
        return facilities.map { it.toDto() }
    }

    //    fun getUserPermissionFromAuthentication(authentication: Authentication): Permission {
//        val principal = authentication.principal as UserDetails // UserDetails로 캐스팅
//        val authorities = principal.authorities // 권한을 가져옴
//
//        // 권한을 기반으로 Permission 객체를 생성
//        val userPermissions = authorities.map { it.authority }
//
//        // 권한 목록에서 Permission 객체 생성
//        val permission = userPermissions.fold(Permission(0)) { acc, authority ->
//            acc + Permissions.valueOf(authority).permission
//        }
//
//        return permission
//    }



    //    override fun findFacilitiesByName(name: String, pageable: PageRequest): SlicedResponse<FacilityDto> {
//        val slice = facilitySearchRepository.findByName(name, ElasticPageRequest(pageable.size, pageable.page))
//        val found = facilityJpaRepository.findFacilitiesByIdIn(slice.data.map { it.id })
//
//        return SlicedResponse(slice.hasNext, slice.size, found.map { it.toDto() })
//    }

    override fun findFacilitiesByAddress(address: String, pageable: PageRequest): SlicedResponse<FacilityDto> {
        val slice = facilitySearchRepository.findByAddress(address, ElasticPageRequest(pageable.size, pageable.page))
        val found = facilityJpaRepository.findFacilitiesByIdIn(slice.data.map { it.id })

        return SlicedResponse(slice.hasNext, slice.size, found.map { it.toDto() })
    }

    override fun findById(id: Long): FacilityDto {
        val facility = facilityJpaRepository.findSafe(id)
        return facility.toDto()
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun createFacility(facilityCreateDto: FacilityCreateDto): FacilityDto {
        val (
            name: String,
            type: FacilityType,
            status: FacilityStatus,
            address: String,
            latitude: Double,
            longitude: Double,
            department: String,
            fixture: String,
            poleFormat: String,
            dimmer: String,
            memo: String,

            phoneNumber: String,
            escoStatus: String,
            powerConsumption: String,
            billingType: String,
            poleNumber: String,
        ) = facilityCreateDto

        val facility = facilityJpaRepository.save(
            Facility(
                name,
                type,
                status,
                address,
                latitude,
                longitude,

                department,
                fixture,
                poleFormat,
                dimmer,

                image = null,
                memo,

                phoneNumber,
                escoStatus,
                powerConsumption,
                billingType,
                poleNumber,
            )
        )
        facilitySearchRepository.save(FacilityDocument.fromEntity(facility))

        return facility.toDto()
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun editFacility(id: Long, facilityEditDto: FacilityEditDto, multipartFile: MultipartFile?): FacilityDto {
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
            poleNumberOrNull: String?,

        ) = facilityEditDto

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
                poleNumber = poleNumberOrNull ?: facility.powerConsumption
            }
        )
        facilitySearchRepository.save(FacilityDocument.fromEntity(saved))

        return saved.toDto()
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun deleteFacility(id: Long) {
        val facility = facilityJpaRepository.findSafe(id)

        facilityJpaRepository.delete(facility)
        facilitySearchRepository.delete(FacilityDocument.fromEntity(facility))
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun setupFacilityRemote(id: Long, dto: FacilityRemoteInfoCreateDto): FacilityRemoteInfoDto {
        val (
            remoteId: Long,
            phone: String,
            number: Int,
        ) = dto
        val facility = facilityJpaRepository.findSafe(id)

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

    override fun getFacilityRemote(id: Long): FacilityRemoteInfoDto {
        val facility = facilityJpaRepository.findSafe(id)

        val remoteInfo = facility.remoteInfo
            ?: throw CustomException(FacilityExceptionDetails.NO_REMOTE_INFO)

        return remoteInfo.toDto()
    }

    override fun toggleFacilityLight(id: Long, dto: FacilityLightToggleDto) {
        val facility = facilityJpaRepository.findSafe(id)

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

    override fun elasticRefresh() {
        val all = facilityJpaRepository.findAll()
        for (facility in all) {
            facilitySearchRepository.save(FacilityDocument.fromEntity(facility))
        }
    }


    fun getStringValue(cell: Cell?): String {
        return when (cell?.cellType) {
            CellType.STRING -> cell.stringCellValue.trim()  // 문자열 그대로 가져옴
            CellType.NUMERIC -> cell.numericCellValue.toString().trim()  // 숫자는 문자열로 변환
            CellType.BOOLEAN -> cell.booleanCellValue.toString().trim()  // 불린 값도 변환
            else -> ""  // 비어 있으면 빈 문자열 반환
        }
    }

    override fun saveFacilitiesFromExcel(multipartFile: MultipartFile) {
        val inputStream: InputStream = multipartFile.inputStream
        val workbook = WorkbookFactory.create(inputStream)
        val sheet = workbook.getSheetAt(0)
        val facilities = mutableListOf<Facility>()

        try {
            val rowIterator = sheet.iterator()
            if (rowIterator.hasNext()) rowIterator.next() // 헤더 스킵

            while (rowIterator.hasNext()) {
                val row = rowIterator.next()
                try {
                    val facilityExcelDto = FacilityExcelDto(
                        name = getStringValue(row.getCell(0)),               // 시설물 명치
                        address = getStringValue(row.getCell(1)),            // 도로명 주소
                        phoneNumber = getStringValue(row.getCell(2)),        // 전화번호
                        type = FacilityTypeConverter.getFacilityTypeFromDisplayName(getStringValue(row.getCell(3))), // 조명 구분
                        fixture = getStringValue(row.getCell(4)),            // 등기구 형태
                        dimmer = getStringValue(row.getCell(5)),             // 점멸기
                        escoStatus = getStringValue(row.getCell(6)),         // ESCO
                        powerConsumption = getStringValue(row.getCell(7)),   // 소비 전력
                        billingType = getStringValue(row.getCell(8)),        // 요금 형태
                        department = getStringValue(row.getCell(9)),        // 관리부서
                        poleFormat = getStringValue(row.getCell(10)),        // 전주번호
                        poleNumber = getStringValue(row.getCell(11)),        // 전주 번호
                        memo = getStringValue(row.getCell(12))               // 메모

                    )

                    val facility = Facility(
                        name = facilityExcelDto.name,
                        type = facilityExcelDto.type,
                        status = null,
                        address = facilityExcelDto.address,
                        latitude = row.getCell(13)?.numericCellValue ?: 0.0,   // 위도
                        longitude = row.getCell(14)?.numericCellValue ?: 0.0,  // 경도
                        department = facilityExcelDto.department,
                        fixture = facilityExcelDto.fixture,
                        poleFormat = facilityExcelDto.poleFormat,
                        dimmer = facilityExcelDto.dimmer,
                        memo = facilityExcelDto.memo,
                        phoneNumber = facilityExcelDto.phoneNumber,
                        escoStatus = facilityExcelDto.escoStatus,
                        powerConsumption = facilityExcelDto.powerConsumption,
                        billingType = facilityExcelDto.billingType,
                        poleNumber = facilityExcelDto.poleNumber,
                        image = null
                    )
                    facilities.add(facility)

                } catch (e: Exception) {
                    null
                }?: continue
            }

            facilityJpaRepository.saveAll(facilities)
            println("✅ ${facilities.size}개의 시설 데이터 저장 완료!")
        } finally {
            workbook.close()
        }
    }


}