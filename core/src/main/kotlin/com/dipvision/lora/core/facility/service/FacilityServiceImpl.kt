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
import com.dipvision.lora.core.facility.findSafe
import com.dipvision.lora.core.facility.repository.FacilityJpaRepository
import com.dipvision.lora.core.facility.repository.FacilitySearchRepository
import com.dipvision.lora.core.facility.toDto
import com.dipvision.lora.core.member.MemberHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true, rollbackFor = [Exception::class])
class FacilityServiceImpl(
    private val facilityJpaRepository: FacilityJpaRepository,
    private val facilitySearchRepository: FacilitySearchRepository,

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
        pageable: com.dipvision.lora.common.page.PageRequest,
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

    @Transactional(rollbackFor = [Exception::class])
    override fun createFacility(facilityCreateDto: FacilityCreateDto): FacilityDto {
        val (
            name: String,
            type: FacilityType,
            status: FacilityStatus,
            address: String,
            latitude: Double,
            longitude: Double,
        ) = facilityCreateDto

        memberHolder.getUserPermission().validate(Permissions.CREATE_FACILITY)

        val facility = facilityJpaRepository.save(
            Facility(
                name,
                type,
                status,
                address,
                latitude,
                longitude,
            )
        )
        facilitySearchRepository.save(FacilityDocument.fromEntity(facility))

        return facility.toDto()
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun editFacility(id: Long, facilityEditDto: FacilityEditDto): FacilityDto {
        memberHolder.getUserPermission().validate(Permissions.READ_FACILITY)
        val facility = facilityJpaRepository.findSafe(id)

        val (
            nameOrNull: String?,
            typeOrNull: FacilityType?,
            statusOrNull: FacilityStatus?,
            addressOrNull: String?,
            latitudeOrNull: Double?,
            longitudeOrNull: Double?,
        ) = facilityEditDto

        memberHolder.getUserPermission().validate(Permissions.UPDATE_FACILITY)
        val saved = facilityJpaRepository.save(
            facility.apply {
                name = nameOrNull ?: facility.name
                type = typeOrNull ?: facility.type
                status = statusOrNull ?: facility.status

                address = addressOrNull ?: facility.address
                latitude = latitudeOrNull ?: facility.latitude
                longitude = longitudeOrNull ?: facility.longitude
            }
        )
        facilitySearchRepository.save(FacilityDocument.fromEntity(saved))

        return saved.toDto()
    }

    override fun deleteFacility(id: Long) {
        memberHolder.getUserPermission().validate(Permissions.READ_FACILITY)
        val facility = facilityJpaRepository.findSafe(id)

        memberHolder.getUserPermission().validate(Permissions.DELETE_FACILITY)
        facilityJpaRepository.delete(facility)
        facilitySearchRepository.delete(FacilityDocument.fromEntity(facility))
    }
}